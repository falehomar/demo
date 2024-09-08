package my.demo.userservice;

import lombok.Builder;
import lombok.Data;
import my.demo.common.Photo;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController()
public class PhotoUploadApi {

    PhotoDataService photoDataService;
    public PhotoUploadApi(PhotoDataService photoDataService) {
        this.photoDataService = photoDataService;
    }



    @PostMapping(path = "/photo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PhotoUploadResponse> uploadPhoto(@RequestPart("items") Flux<FilePart> filePartFlux){
        return filePartFlux.flatMap(filePart -> photoDataService.save(filePart)).collectList()
                .map(photos -> PhotoUploadResponse.builder().photos(photos).build());
    }

    @GetMapping(path="/photo")
    public Flux<String> getPhoto(){
        return Flux.just("Photo");
    }

    @Data
    @Builder(toBuilder = true)
    public static class PhotoListResponse {
        List<Photo> photos;
    }
    @GetMapping(path="/photos")
    public Mono<PhotoListResponse> getPhotos(){
        return photoDataService.findAll().collectList()
                .map(photos -> PhotoListResponse.builder().photos(photos).build());
    }
}
