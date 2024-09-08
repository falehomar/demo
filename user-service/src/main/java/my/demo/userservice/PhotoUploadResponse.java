package my.demo.userservice;

import lombok.*;
import my.demo.common.Photo;

import java.util.List;
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class PhotoUploadResponse {
    List<Photo> photos;
}
