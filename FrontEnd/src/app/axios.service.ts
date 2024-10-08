import { Injectable } from '@angular/core';
import axios from 'axios';
import { Company } from './company';

@Injectable({
  providedIn: 'root'
})
export class AxiosService {
  private baseUrl = 'http://localhost:8080';
  constructor() {
    axios.defaults.baseURL = 'http://localhost:8080';
    //axios.defaults.headers.post['Content-Type'] = 'application/json';
  }

  getAuthToken(): string | null {
    return window.localStorage.getItem("auth_token");
  }

  setAuthToken(token: string | null): void {
    if (token !== null) {
      window.localStorage.setItem("auth_token", token);
    } else {
      window.localStorage.removeItem("auth_token");
    }
  }

  //gestione upload con promise
   requestFile(method: string, url: string, data: any ): Promise<any> {
    
  let headers = {}
    // Aggiungi il token di autenticazione, se presente
    const token = this.getAuthToken();
    if (token !== null) {
     headers = {
                   "Authorization" : "Bearer "+token,
                   'Content-Type': 'multipart/form-data'

      };
    }
 // Imposta 'Content-Type' solo se 'data' non è una istanza di FormData
 
 // Log headers e altri dettagli per il debugging
 console.log("Request Method:", method);
 console.log("Request URL:", url);
 console.log("Request Headers:", headers);
 console.log("Request Data:", data);
     
      return axios({
        method: method,
        url: url,
        data: data,
        headers: headers
       
    
      });
      
  
  }
  getRows() {
    return axios.get(`${this.baseUrl}/getRows`);
  }


  //gestione promise login  e register 
  
async request(method: string, url: string, data: any = null): Promise<any> {
    const headers: any = {};

    // Aggiungi il token di autenticazione, se presente
    const token = this.getAuthToken();
    if (token !== null) {
      headers['Authorization'] = "Bearer " + token;
    }
 // Imposta 'Content-Type' solo se 'data' non è una istanza di FormData
 if (!(data instanceof FormData)) {
  headers['Content-Type'] = 'application/json';
}
console.log("Request Method:", method);
 console.log("Request URL:", url);
 console.log("Request Headers:", headers);
 console.log("Request Data:", data);
    try {
      const response = await axios({
        method: method,
        url: url,
        data: data,
        headers: headers
       
    
      });
      console.log("response Data:",response.data);
      return response.data;
    } catch (error) {
      console.error('Error in request:', error);
      throw error;
    }
  }
  async uploadFile(file: File): Promise<any> {
    const formData = new FormData();
    formData.append('file', file);

    const headers = {
      'Authorization': `Bearer ${this.getAuthToken()}`,
      'Content-Type': 'multipart/form-data'
    };

    try {
      const response = await axios.post('/api/files/Upload', formData, { headers });
      return response.data;
    } catch (error) {
      console.error('Error uploading file:', error);
      throw error;
    }
  }
// Metodo per ottenere le società
async getCompanies(): Promise<Company[]> {
  try {
    // Assumendo che 'this.request' ritorni una Promise che risolve in un array di 'Company'
    const response = await this.request('get', '/api/companies');
    
    // Verifica se la risposta è del tipo corretto
    if (Array.isArray(response)) {
      return response;
    } else {
      throw new Error('Response is not an array of companies');
    }
  } catch (error) {
    // Gestione degli errori specifici
    if (error instanceof TypeError) {
      console.error('Type Error:', error.message);
    } else if (error instanceof SyntaxError) {
      console.error('Syntax Error:', error.message);
    } else {
      console.error('Unexpected Error:', error);
    }
    
    // Rilancia l'errore per propagare il fallimento
    throw error;
  }
}}
