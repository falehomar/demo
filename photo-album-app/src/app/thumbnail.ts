export interface Dimention{
  width: number;
  height: number;
}
export interface Photo {
  id: string;
  dimension: Dimention;
  size: number;
  thumbnail: string;
}
export interface PhotoListResponse {
  photos: Photo[];
}
