import { Component } from '@angular/core';
import { AxiosService } from '../axios.service';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html'
})
export class FileUploadComponent {
  maxFileSize = 10 * 1024 * 1024; // 10 MB
  errorMessage: string | null = null;
  warningMessage: string | null = null;
  successMessage: string | null = null;
  csvData: any[] = [];

  constructor(private axiosService: AxiosService) {}

  async uploadFile(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      if (file.size > this.maxFileSize) {
        this.errorMessage = 'Il file selezionato supera la dimensione massima di 10 MB.';
      } else {
        this.errorMessage = '';
        this.warningMessage = '';
        this.successMessage = '';

        try {
          const formData = new FormData();
          console.log("ciao1")
          formData.append('file', file);
          // Aggiungi eventuali parametri extra a formData se necessario

          // Invia tramite axios service
          const response = await this.axiosService.requestFile('POST', '/api/uploadFile', formData);
          console.log(response);

          if (response.success) {
            this.csvData = response.data || [];
            this.successMessage = response.message;
          } else {
            this.csvData = [];
            if (response.data.message === 'Header non riconosciuti') {
              this.warningMessage = response.message;
              console.log("ciao")
              console.log('Header non riconosciuti:', response.data);
            } else if (response.message === 'Errors in CSV') {
              this.errorMessage = 'Ci sono errori nel file CSV.';
              console.log('Errors in CSV:', response.data);
            } else {
              this.errorMessage = response.message;
            }
          }

          console.log('File caricato con successo', response);
        } catch (error) {
          console.error('Errore nel caricamento del file', error);
          this.errorMessage = 'Errore nel caricamento del file';
          this.csvData = [];
        }
      }
    }
  }

  ngOnInit() {}

  getHashKeys() {
    return this.csvData.length > 0 ? Object.keys(this.csvData[0]) : [];
  }
}
