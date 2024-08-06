import { Component } from '@angular/core';
import { AxiosService } from '../axios.service';

@Component({
  selector: 'app-file-upload-component',
  templateUrl: './file-upload-component.component.html',
  styleUrls: ['./file-upload-component.component.css']
})
export class FileUploadComponentComponent {

   private selezionato:File |null = null;
   uploadMessage: string | null = null;
   //iniezone axios
   constructor(private axiosService:AxiosService){}

   onFileChange(event:any){
    const file = event.target.files[0];
    if(file){
      this.selezionato  = file;
    }
   }
async upload(){

if(this.selezionato) {

try{
const response = await this.axiosService.uploadFile(this.selezionato);
this.uploadMessage = 'File uploaded successfully!';
console.log('File uploaded successfully:', response);
      } catch (error) {
        console.error('Error uploading file:', error);
        this.uploadMessage = 'No file selected';
      }
    } else {
      
      console.error('No file selected');
      this.uploadMessage = 'File uploaded no successfully!';
    }
  }

}




