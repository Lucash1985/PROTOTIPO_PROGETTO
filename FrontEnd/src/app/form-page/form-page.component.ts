import { Component } from '@angular/core';
import { FileUploadComponentComponent } from '../file-upload-component/file-upload-component.component';
@Component({
  selector: 'app-form-page',
  templateUrl: './form-page.component.html',
  styleUrls: ['./form-page.component.css']
})
export class FormPageComponent {
  // Dichiarazione dei campi
  nominativo = '';
  indirizzo = '';
  cap = '';
  provincia = '';
  mittenteNominativo = '';
  mittenteIndirizzo = '';
  mittenteCitta = '';
  mittenteStato = '';
  destinatarioNominativo = '';
  destinatarioIndirizzo = '';
  destinatarioCap = '';
  destinatarioProvincia = '';
  destinatarioCitta = '';
  destinatarioStato = '';

  // Variabile per gestire la visibilità della form
  isFormVisible = true;

  // Metodo di invio
  onSubmit(form: any) {
    if (form.valid) {
      // Gestisci l'invio della form
      console.log('Form Submitted', form.value);
    }
  }

  // Metodo di uscita
  onExit() {
    if (confirm('Sei sicuro di voler uscire? Tutti i dati non salvati saranno persi.')) {
      this.isFormVisible = false; // Nasconde la form
      // Opzionalmente, puoi resettare i campi della form o eseguire altre azioni
      this.resetForm();
    }
  }

  // Metodo per rientrare nella form
  onEnter() {
    this.isFormVisible = true; // Mostra la form
  }

  // Metodo per resettare la form
  resetForm() {
    this.nominativo = '';
    this.indirizzo = '';
    this.cap = '';
    this.provincia = '';
    this.mittenteNominativo = '';
    this.mittenteIndirizzo = '';
    this.mittenteCitta = '';
    this.mittenteStato = '';
    this.destinatarioNominativo = '';
    this.destinatarioIndirizzo = '';
    this.destinatarioCap = '';
    this.destinatarioProvincia = '';
    this.destinatarioCitta = '';
    this.destinatarioStato = '';
  }
}
