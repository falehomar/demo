import {Routes} from '@angular/router';
import {PhotoIconsComponent} from "./photo-icons/photo-icons.component";
import {PhotoUploadComponent} from "./photo-upload/photo-upload.component";

export const routes: Routes = [
  {
    path:'photo-icons',
    component: PhotoIconsComponent,
    children:[
      {
        path:'photo-upload',
        component: PhotoUploadComponent,
      },
    ]
  },
  {
    path:'**',
    redirectTo:'photo-icons'
  },

];
