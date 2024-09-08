import {Component, OnInit} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatToolbar} from "@angular/material/toolbar";
import {RouterLink, RouterOutlet} from "@angular/router";
import {PhotoUploadService} from "../photo-upload.service";
import {Thumbnail} from "../thumbnail";
import {ThumbnailComponent} from "../thumbnail/thumbnail.component";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-photo-icons',
  standalone: true,
  imports: [
    MatButton,
    MatToolbar,
    RouterOutlet,
    RouterLink,
    ThumbnailComponent,
    NgForOf
  ],
  templateUrl: './photo-icons.component.html',
  styleUrl: './photo-icons.component.css'
})
export class PhotoIconsComponent implements OnInit {

  thumbnails : Thumbnail[] = [
    {id:"",url:"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADgAAAA4CAMAAACfWMssAAAAqFBMVEX/////AAD/mQD/TwD/lwD/kgD/lAD/mwD/SQD/UgD/TAD+//3/kAD+i5D/IwD/gAD+ABH/rUj95uj+8vT+ys/+urz+wMT+2d//79n/4L7/3Lj/6c7/+e3+3uL+hor+V1//oBv+yY3+8+L+X2b+FyP+1aj+vG79r7X+tFf/PgD9lZz/YgD+f4b/OEL+wHj/iAD/uGL+pqz/cgD/pS790Z3/MTj+R07/d3z/YQIzAAABaklEQVRIie2V3VaCQBSFgfmRgdFEBQEzRbSy0jAy3//NEljkzDlAV13FvmStb+2zZ58ZDKNXr/+mycz3g7nywQ2jOFrcd1PLh7FZaeVXVOwxyRiTcp1s2s1SU9H2iu4GjFqVKGePLbZPJlD6/IOV4nTRxA0hZ+4FtzRRtsPcC+Js4giqk5aMIDfDfoQQ+3UAPDk8IsSNBCnIO2BJvd8HLSUAaMlYA8fIsOKulnBYzTJoTFjIgSktpqZ8Q6BDasGD1So5IFDUnC3h8Rw7I97AzpC4DPLH4LYj4zvMmClg2gGCRbd4ooAnBNp1jwL1qN6tSWtI5wNGHLjq6qwQWYOwRm1Sw5gjcN+8cZSD96Nt6fDCua5OnlFKUXQBDHlmhOAqT1GXI1w+94BdSeJNB9efyqyBM9A7d1iGubxlpJx/NmJFnRcFK5/yOGeM06s4o0nXb2AaDNPz+etyWtZfNrujl+dZsmieslevXkjfEVAZQDMVRI8AAAAASUVORK5CYII=",size:0,photoSize:0}
  ];

  constructor(private photoUploadService: PhotoUploadService,) {
  }
  ngOnInit(){
    this.photoUploadService.getThumbnails()
  }
}
