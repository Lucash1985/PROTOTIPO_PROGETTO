import { Component } from '@angular/core';
import { Input } from '@angular/core';
@Component({
  selector: 'app-breadcrumb',
  templateUrl: './breadcrumb.component.html',
  styleUrls: ['./breadcrumb.component.css']
})
export class BreadcrumbComponent {
  @Input() breadcrumb: string = ''; // Input per il testo del breadcrumb
}
