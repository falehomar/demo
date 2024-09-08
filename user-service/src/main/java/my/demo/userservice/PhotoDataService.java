package my.demo.userservice;

import my.demo.common.Photo;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsOperations;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PhotoDataService {
    private ReactiveMongoOperations mongoOperations;
    private KafkaTemplate<String, Photo> kafkaTemplate;

    ReactiveGridFsOperations gridFsOperations;
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
                .flatMap(photo -> mongoOperations.save(photo))
                .doOnNext(photo -> kafkaTemplate.send("photos",photo.getId().toHexString(),photo));

    }
    public Flux<Photo> findAll() {
        return mongoOperations.find(Query.query(Criteria.where("_id").exists(true)),Photo.class);
    }
}
