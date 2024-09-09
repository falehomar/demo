package my.demo.photoservice;
import static org.mockito.Mockito.when;

import my.demo.common.Photo;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Profiles;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;

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
}
