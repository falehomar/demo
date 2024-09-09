package my.demo.photoservice;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePartEvent;
import org.springframework.http.codec.multipart.FormPartEvent;
import org.springframework.http.codec.multipart.PartEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@RestController()
public class PhotoUploadApi {

    PhotoDataService photoDataService;

    public PhotoUploadApi(PhotoDataService photoDataService) {
        this.photoDataService = photoDataService;
    }


    @PostMapping(path = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<PhotoUploadResponse> uploadPhoto(@RequestBody Flux<PartEvent> partEventFlux) {

        return partEventFlux.windowUntil(PartEvent::isLast)
                .concatMap(p -> p.switchOnFirst(((signal, partEvents) -> {

                    if (signal.hasValue()) {
                        PartEvent partEvent = signal.get();
                        if (partEvent instanceof FilePartEvent filePartEvent) {
                            log.info("FilePartEvent");
                                    return photoDataService.save(partEvents.map(PartEvent::content))
                                    .map(photos -> PhotoUploadResponse.builder().photos(Collections.singletonList(photos)).build());
                        } else if (partEvent instanceof FormPartEvent formPartEvent) {
                            log.info("FormFieldPart {}: {}", formPartEvent.name(),formPartEvent.value());
                            return Mono.empty();
                        } else {
                            log.warn("unknown event");
                            return Mono.empty();
                        }
                    } else {
                        return Mono.empty();
                    }
                })));
    }

    @GetMapping(path = "/photo/{id}/data", produces = MediaType.IMAGE_PNG_VALUE)
    public Mono<ResponseEntity<Flux<DataBuffer>>> getPhotoData(@PathVariable("id") String id, ServerWebExchange exchange) {
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=image.png");

        return Mono.just(new ResponseEntity<>(photoDataService.downloadPhoto(id), HttpStatus.OK));
    }

    @GetMapping(path = "/photos")
    public Mono<PhotoListResponse> getPhotos() {
        return photoDataService.findAll().collectList()
                .map(photos -> PhotoListResponse.builder().photos(photos).build());
    }
}
