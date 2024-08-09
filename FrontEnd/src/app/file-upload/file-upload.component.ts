import { Component } from '@angular/core';
import { AxiosService } from '../axios.service';
import { Destinatario } from '../destinatario/destinatario.component';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html'
})
export class FileUploadComponent {
  maxFileSize = 10 * 1024 * 1024; // 10 MB
  errorMessage: string | null = null;
  warningMessage: string | null = null;
  successMessage: string | null = null;
  headerErrors: string[] = [];
  destinatari: Destinatario[] = [];

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
        this.headerErrors = [];

        try {
          const formData = new FormData();
          formData.append('file', file);

          // Invia tramite axios service
          const response = await this.axiosService.requestFile('POST', '/api/uploadFile', formData);

          // Log the response structure
          console.log('Response:', response);

          if (response.data.success) {
            this.destinatari = Object.values(response.data.data) || [];
            this.successMessage = response.data.message;
          } else {
            this.destinatari = [];
            if (response.data.message === 'Header non riconosciuti') {
              this.headerErrors = response.data.data || [];
              this.warningMessage = 'Header non riconosciuti: ' + this.headerErrors.join(', ');
            } else if (response.data.message === 'Errors in CSV') {
              this.errorMessage = 'Ci sono errori nel file CSV.';
              // Aggiungi qui la gestione degli errori e il caricamento delle righe se necessario
              const destinatarioMapControlTotal = response.data.data || {};
              this.destinatari = Object.values(destinatarioMapControlTotal) as Destinatario[];
            } else {
              this.errorMessage = response.data.message;
            }
          }
        } catch (error) {
          this.errorMessage = 'Errore nel caricamento del file';
          console.error('File upload error:', error);
          this.destinatari = [];
        }
      }
    }
  }
}
