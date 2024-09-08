package my.demo.photoservice;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
@Configuration
public class ReactiveApplicationConfiguration extends AbstractReactiveMongoConfiguration {

    public static final String DATABASE_NAME = "photo-album";

    @Bean
    MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }

//    @Bean
//    ReactiveMongoOperations mongoTemplate(MongoClient mongoClient) {
//        return new ReactiveMongoTemplate(mongoClient, DATABASE_NAME);
//    }

    @Bean
    public ReactiveGridFsTemplate reactiveGridFsTemplate(ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory, MongoConverter mongoConverter) {
        return new ReactiveGridFsTemplate(reactiveMongoDatabaseFactory, mongoConverter);
    }

    @Override
    protected String getDatabaseName() {
        return DATABASE_NAME;
    }
}
