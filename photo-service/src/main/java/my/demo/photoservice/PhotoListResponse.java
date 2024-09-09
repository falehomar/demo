package my.demo.photoservice;

import lombok.Builder;
import lombok.Data;
import my.demo.common.Photo;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class PhotoListResponse {
    List<Photo> photos;
}
