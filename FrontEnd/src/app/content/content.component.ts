import { Component } from '@angular/core';
import { AxiosService } from '../axios.service';
import * as bcrypt from 'bcryptjs';

@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.css']
})
export class ContentComponent {
  componentToShow: string = "welcome";
  errorMessage: string = '';
  isAdmin: boolean = false; // Variabile per tenere traccia se l'utente è ADMIN
  companyId: number=0;
  breadcrumb: string = 'Home'; // Variabile per il breadcrumb
  id :number=0
  constructor(private axiosService: AxiosService) {}

  showComponent(componentToShow: string): void {
    this.componentToShow = componentToShow;

    this.updateBreadcrumb(); 
  }

  hashPassword(password: string): string {
    const salt = bcrypt.genSaltSync(10);
    return bcrypt.hashSync(password, salt);
  }

  onLogin(input: any): void {
    if (!input.login || !input.password) {
      this.errorMessage = 'Please enter both login and password.';
      return;
    }

    console.log('Attempting to login with:', input);

    this.axiosService.request(
      "POST",
      "/login",
      {
        login: input.login,
        password: input.password
      }).then(
      response => {
        console.log('Login successful:', response);
        this.axiosService.setAuthToken(response.token);
        this.errorMessage = '';

        // Aggiorna il ruolo e la visibilità dei componenti in base alla risposta
        this.isAdmin = response.roles.includes('ADMIN');
        if (this.isAdmin) {
          this.componentToShow = "register"; // Mostra la vista dei messaggi o una dashboard
        } else {
          this.componentToShow = "messages"; // Mostra la vista dei messaggi per altri ruoli
        }
        console.log(this.componentToShow);
      }).catch(
      error => {
        console.error('Login error:', error);
        this.axiosService.setAuthToken(null);
        if (error.response && error.response.data && error.response.data.message) {
          this.errorMessage = error.response.data.message;
        } else {
          this.errorMessage = 'Login failed. Please try again.';
        }
        this.componentToShow = "welcome";
      }
    );
  }

  onRegister(input: any): void {


    console.log('Dati di input:', input);
    console.log('Valore di companyId:', input.companyId);
      // Assicurati che companyId sia un array, anche se contiene un solo elemento
  //const companyIdsArray = Array.isArray(input.companyId) ? input.companyId : [input.companyId];
 

    const hashedPassword = this.hashPassword(input.password);
    this.axiosService.request(
      "POST",
      "/register",
      {
        login: input.login,
        password: input.password,
        role: input.role,
        companyId: input.companyId
      }).then(
      response => {
        this.axiosService.setAuthToken(response.data.token);
        this.componentToShow = "messages";
      }).catch(
      error => {
        this.axiosService.setAuthToken(null);
        this.componentToShow = "welcome";
      }
    );
  }
   // Aggiorna il breadcrumb in base al componente visualizzato
   private updateBreadcrumb(): void {
    switch (this.componentToShow) {
      case 'welcome':
        this.breadcrumb = 'Home';
        break;
      case 'login':
        this.breadcrumb = 'Login';
        break;
      case 'register':
        this.breadcrumb = 'Register';
        break;
      case 'messages':
        this.breadcrumb = this.isAdmin ? 'Messages - Admin' : 'Messages';
        break;
      default:
        this.breadcrumb = 'Home';
    }
  }
   // Gestisci l'evento di ritorno alla vista di login
   handleBackToLogin(): void {
    this.showComponent('messages');
  }
}
