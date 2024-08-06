import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ButtonsComponent } from './buttons/buttons.component';
import { HeaderComponent } from './header/header.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { WelcomeContentComponent } from './welcome-content/welcome-content.component';
import { AuthContentComponent } from './auth-content/auth-content.component';
import { ContentComponent } from './content/content.component';

import { AxiosService } from './axios.service';
import { FileUploadComponent } from './file-upload/file-upload.component';
import { HttpClientModule } from '@angular/common/http';
import { RegisterComponent } from './register/register.component';
import { TavolaComponent } from './tavola/tavola.component';
import { Routes } from '@angular/router';
import { FormPageComponent } from './form-page/form-page.component';
import { BreadcrumbComponent } from './breadcrumb/breadcrumb.component';
import { FileUploadComponentComponent } from './file-upload-component/file-upload-component.component';
import { FormPage1Component } from './form-page1/form-page1.component';


@NgModule({
  declarations: [
    AppComponent,
    ButtonsComponent,
    HeaderComponent,
    LoginFormComponent,
    WelcomeContentComponent,
    AuthContentComponent,
    ContentComponent,
    FileUploadComponent,
    RegisterComponent,
    TavolaComponent,
    FormPageComponent,
    BreadcrumbComponent,
    FileUploadComponentComponent,
    FormPage1Component

   
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
    
  ],
  
  providers: [AxiosService],
  bootstrap: [AppComponent]
})
export class AppModule { }
