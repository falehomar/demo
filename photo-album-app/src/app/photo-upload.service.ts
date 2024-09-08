import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {EMPTY} from "rxjs";

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

  getThumbnails() {
    return this.httpClient.get("http://localhost:8080/thumbnails")

  }
}
