package my.demo.photoservice;

import my.demo.common.Photo;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsOperations;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsResource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
public class PhotoDataService {
    private final ReactiveMongoOperations mongoOperations;
    private final KafkaTemplate<String, Photo> kafkaTemplate;
    private final ReactiveGridFsOperations gridFsOperations;

    public PhotoDataService(ReactiveMongoOperations mongoOperations,
                            ReactiveGridFsOperations gridFsOperations,
                            KafkaTemplate<String, Photo> kafkaTemplate) {
        this.mongoOperations = mongoOperations;
        this.gridFsOperations = gridFsOperations;
        this.kafkaTemplate = kafkaTemplate;
    }


    public Mono<Photo> save(FilePart filePart) {
        return gridFsOperations.store(filePart.content(), filePart.filename())
                .map(objectId -> Photo.builder().dataFile(objectId).build())
                .flatMap(mongoOperations::save)
                .doOnNext(photo -> kafkaTemplate.send("photos",photo.getId().toHexString(),photo));

    }
    public Mono<Photo> save(Flux<DataBuffer> dataBuffer) {
        return gridFsOperations.store(dataBuffer,"FILEUPLAOD")
                .map(objectId -> Photo.builder().dataFile(objectId).build())
                .flatMap(mongoOperations::save)
                .doOnNext(photo -> kafkaTemplate.send("photos",photo.getId().toHexString(),photo));

    }
    public Flux<Photo> findAll() {
        return mongoOperations.find(Query.query(Criteria.where("_id").exists(true)),Photo.class);
    }

    public Flux<DataBuffer> downloadPhoto(String id) {
        return mongoOperations.findOne(Query.query(Criteria.where("_id").is(id)),Photo.class)
                .map(Photo::getDataFile)
                .flatMap(objectId -> gridFsOperations.findOne(Query.query(Criteria.where("_id").is(objectId))))
                .flatMap(gridFsOperations::getResource)
                .map(ReactiveGridFsResource::getDownloadStream)
                .flux()
                .flatMap(Function.identity())
                .log()
                ;
    }
}
