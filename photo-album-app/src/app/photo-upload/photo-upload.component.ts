import {Component} from '@angular/core';
import {MatMiniFabButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {NgStyle} from "@angular/common";
import {PhotoUploadService} from "../photo-upload.service";
import {ReactiveFormsModule} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-photo-upload',
  standalone: true,
  imports: [
    MatMiniFabButton,
    MatIcon,
    NgStyle,
    ReactiveFormsModule
  ],
  templateUrl: './photo-upload.component.html',
  styleUrl: './photo-upload.component.css'
})
export class PhotoUploadComponent {
  fileName: string | undefined;

  constructor(private photoUploadService: PhotoUploadService,private router: Router) {
  }

  onFileSelected(event: Event) {
    if (event.target){
      let t = (<HTMLInputElement>event.target)
      if (t.files && t.files[0]){
        const file:File =t.files[0]
        if (file){
          this.fileName = file.name;
          this.photoUploadService.uploadPhoto(file)
            .subscribe((result)=>this.router.navigateByUrl("../"))
        }
      }

    }




  }
}
