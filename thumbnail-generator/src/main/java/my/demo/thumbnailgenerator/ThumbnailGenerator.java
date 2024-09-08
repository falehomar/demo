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
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Function;

@Component
@Slf4j
public class ThumbnailGenerator {

    ReactiveGridFsOperations gridFsOperations;
    private final ReactiveMongoOperations mongoOperations;

    public ThumbnailGenerator(ReactiveGridFsOperations gridFsOperations, ReactiveMongoOperations mongoOperations) {
        this.gridFsOperations = gridFsOperations;
        this.mongoOperations = mongoOperations;
    }

    @KafkaListener(topics = {"photos"},groupId = "ThumbnailGenerator")
    public void generateThumbnail(Photo photo) {
      log.info("Thumbnail generation of {}",photo);
        this.gridFsOperations.findFirst(Query.query(Criteria.where("_id").is(photo.getDataFile())))
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
                    int width = bufferedImage.getWidth();
                    int height = bufferedImage.getHeight();
                    var aspectRatio = width /height;
                    var tWidth = 75;
                    var tHeight = tWidth/aspectRatio;
                    AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance((double) tWidth /width, (double) tHeight /height), AffineTransformOp.TYPE_BILINEAR);
                    BufferedImage dst = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_INT_RGB);
                    op.filter(bufferedImage, dst);

                    return dst;
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
                .flatMap(dataBuffer -> this.gridFsOperations.store(Mono.just(dataBuffer), photo.getId()+"_thumbnail.png"))
                .flatMap(objectId -> {
                    Photo thumbnail = Photo.builder().dataFile(objectId).build();
                    return this.mongoOperations.save(thumbnail).map(savedThumbnail->this.mongoOperations
                            .findOne(Query.query(Criteria.where("_id").is(photo.getId())), Photo.class)
                            .map(storedPhoto-> this.mongoOperations.save(storedPhoto.toBuilder().thumbnail(savedThumbnail.getId()).build())));
                })
                .flatMap(Function.identity())
                .flatMap(Function.identity())
                .subscribe(result->log.info("Completed thumbnail generation of {}",result));
                ;
    }
}
