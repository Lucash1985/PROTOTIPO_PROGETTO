import { Component } from '@angular/core';
import { Company } from '../company';
import { EventEmitter } from '@angular/core';
import { AxiosService } from '../axios.service';
import { Output } from '@angular/core';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})

  export class RegisterComponent {
    @Output() onSubmitRegisterEvent = new EventEmitter();
    @Output() backToLoginEvent = new EventEmitter<void>();
    login: string = "";
    password: string = "";
    role: string = "";
    roles: string[] = ['USER', 'ADMIN'];
    companies: Company[] = [];
    selectedCompany: number =0;
  
    constructor(private axiosService: AxiosService) {}
  
    ngOnInit() {
      this.loadCompanies();
    }
  
    async loadCompanies() {
      try {
        this.companies = await this.axiosService.getCompanies();
        if (this.companies.length > 0) {
          this.selectedCompany = this.companies[0].id; // Imposta la società predefinita
        }
      } catch (error) {
        console.error('Error loading companies:', error);
      }
    }
  
    onSubmitRegister(): void {
      this.onSubmitRegisterEvent.emit({ "login": this.login, "password": this.password, "role": this.role, "companyId": this.selectedCompany });
    }
    backToLogin(): void {
      this.backToLoginEvent.emit();
    }
  }
  

