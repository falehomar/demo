import {Component, Input} from '@angular/core';
import {Photo} from "../thumbnail";
import {NgOptimizedImage} from "@angular/common";

@Component({
  selector: 'app-thumbnail',
  standalone: true,
  imports: [
    NgOptimizedImage
  ],
  templateUrl: './thumbnail.component.html',
  styleUrl: './thumbnail.component.css'
})
export class ThumbnailComponent {
  static defaultThumbnail: Photo = {
    id:"",
    size:0,
    thumbnail:"",
    dimension: {width:0,height:0}
  }
  @Input({required:true}) photo: Photo = ThumbnailComponent.defaultThumbnail;

}
