import {Component, Input} from '@angular/core';
import {Thumbnail} from "../thumbnail";
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
  static defaultThumbnail: Thumbnail = {
    id:"",
    photoSize: 0,
    url: "",
    size:0
  }
  @Input({required:true}) thumbnail: Thumbnail = ThumbnailComponent.defaultThumbnail;

}
