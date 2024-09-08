import {Component} from '@angular/core';
import {MatMiniFabButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {NgStyle} from "@angular/common";
import {PhotoUploadService} from "../photo-upload.service";
import {FormControl, ReactiveFormsModule} from "@angular/forms";
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
  file = new FormControl();

  constructor(private photoUploadService: PhotoUploadService,private router: Router) {
  }

  onFileSelected($event: Event) {
    console.log("onFileSelected", this.file);

    this.photoUploadService.uploadPhoto(this.file.getRawValue())
      .subscribe((result)=>this.router.navigateByUrl("../"))
  }
}
