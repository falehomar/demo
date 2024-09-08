package my.demo.thumbnailgenerator;

import lombok.extern.slf4j.Slf4j;
import my.demo.common.Photo;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsOperations;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@Slf4j
public class ThumbnailGenerator {

    ReactiveGridFsOperations gridFsOperations;
    private ReactiveMongoOperations mongoOperations;

    public ThumbnailGenerator(ReactiveGridFsOperations gridFsOperations, ReactiveMongoOperations mongoOperations) {
        this.gridFsOperations = gridFsOperations;
        this.mongoOperations = mongoOperations;
    }

    @KafkaListener(topics = {"photos"},groupId = "ThumbnailGenerator")
    public void generateThumbnail(Photo photo) {
      log.info("Thumbnail generation of {}",photo);
        this.gridFsOperations.findFirst(Query.query(Criteria.where("_id").is(photo.getId())))
                .flatMap(this.gridFsOperations::getResource)
                .flatMap(ReactiveGridFsResource::getInputStream)
                .map(inputStream -> {
                    try (inputStream) {
                        return Optional.of(ImageIO.read(inputStream));
                    } catch (IOException e) {
                        log.error(e.getMessage(),e);
                        return Optional.<BufferedImage>empty();
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(bufferedImage -> {

                    return bufferedImage;
                }).map(bufferedImage -> {
                    DefaultDataBuffer dataBuffer = new DefaultDataBufferFactory().wrap("".getBytes(StandardCharsets.UTF_8));
                    try (var outputStream = dataBuffer.asOutputStream()){
                        ImageIO.write(bufferedImage,"png",outputStream);
                        return Optional.of(dataBuffer);
                    } catch (IOException e) {
                        log.error(e.getMessage(),e);
                        return Optional.<DataBuffer>empty();
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(dataBuffer -> this.gridFsOperations.store(Mono.just(dataBuffer), photo.getId()))
                .flatMap(objectId -> {
                    Photo thumbnail = Photo.builder().id(objectId).build();
                    return this.mongoOperations.save(thumbnail).map(savedThumbnail->this.mongoOperations.findOne(Query.query(Criteria.where("_id").is(objectId)), Photo.class)
                            .map(storedPhoto-> this.mongoOperations.save(storedPhoto.toBuilder().thumbnail(savedThumbnail).build())));
                })
                .subscribe()

                ;
    }
}
