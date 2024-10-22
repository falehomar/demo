package my.demo.photoservice;

import my.demo.common.Photo;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient
@WebFluxTest(PhotoUploadApi.class)
class PhotoUploadApiTest {

    @MockBean
    PhotoDataService photoDataService;

    @Test
    void getPhotos(@Autowired WebTestClient client) {
        String id = "123412341234123412341234";
        when(photoDataService.findAll())
                .thenReturn(Flux.just(Photo.builder()
                .id(new ObjectId(id))
                .build()));

        client.get().uri("/photos").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.photos[*].id").isEqualTo(id);
    }

    @Test
    public void uploadPhoto(@Autowired WebTestClient client) {
        when(photoDataService.save(any(Flux.class))).thenReturn(Mono.just(Photo.builder().build()));

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder. part("image", new ClassPathResource("image.png"));


        MultiValueMap<String, HttpEntity<?>> multipartBody = builder. build();

        client.post().uri("/photo")
                .header("Content-Type",MediaType.MULTIPART_FORM_DATA_VALUE)
                .bodyValue(multipartBody)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.photos[*].id").isNotEmpty();
    }
}
