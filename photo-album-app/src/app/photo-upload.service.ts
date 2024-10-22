import {Injectable} from '@angular/core';
import {HttpClient, HttpEvent} from "@angular/common/http";
import {EMPTY, Observable} from "rxjs";
import {Photo, PhotoListResponse} from "./thumbnail";

@Injectable({
  providedIn: 'root'
})
export class PhotoUploadService {

  constructor(
    private httpClient: HttpClient,
  ) { }

  public uploadPhoto(file: File | undefined) {
    console.log(file);
    if (file) {
      let formParams = new FormData();
      formParams.append('items', file)
      return this.httpClient.post('http://localhost:8080/photo', formParams)
    } else {
      console.error("file must not be empty")
      return EMPTY
    }

  }

  getThumbnails() : Observable<PhotoListResponse> {
    return this.httpClient.get<PhotoListResponse>("http://localhost:8080/photos")

  }
}
