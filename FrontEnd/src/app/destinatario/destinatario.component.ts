import { Component, Input } from '@angular/core';

export interface Destinatario {
  ragioneSociale: string;
  nome: string;
  cognome: string;
  cap: string;
  citta: string;
  provincia: string;
  stato: string;
  indirizzo: string;
  completamentoIndirizzo: string;
  completamentoNominativo: string;
  nomeFile: string;
  codiceFiscale: string;
  telefono: string;
}

@Component({
  selector: 'app-destinatario',
  templateUrl: './destinatario.component.html',
  styleUrls: ['./destinatario.component.css']
})
export class DestinatarioComponent {
  @Input() destinatario!: Destinatario;
}
