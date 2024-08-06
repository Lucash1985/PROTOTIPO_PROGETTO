import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-tavola',
  templateUrl: './tavola.component.html',
  styleUrls: ['./tavola.component.css']
})
export class TavolaComponent {
  cards = [
    { id: 1, image: 'https://c7.alamy.com/compit/2d7135b/icona-nera-della-busta-aperta-nel-simbolo-della-posta-in-stile-piatto-2d7135b.jpg' },
    { id: 2, image: 'https://c7.alamy.com/compit/2k80r52/apri-mail-icona-nera-piatta-logo-in-forma-di-contorno-illustrazione-vettoriale-su-sfondo-bianco-isolato-2k80r52.jpg' }
  ];
  constructor(private router: Router) { }
  componentToShow: string = '';
  invioSingolo(cardId: number) {
    console.log(`Invio Singolo per la card ${cardId}`);
    this.componentToShow = 'singolo';

  }

  invioMassivo(cardId: number) {
    console.log(`Invio Massivo per la card ${cardId}`);
    this.componentToShow = 'form';
  }
  goBack() {
    // Cambia la variabile per tornare alla vista delle card
    this.componentToShow = 'cards';
  }
}
