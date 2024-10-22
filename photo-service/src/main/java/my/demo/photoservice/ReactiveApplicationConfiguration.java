package my.demo.photoservice;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
@Configuration
public class ReactiveApplicationConfiguration {

    @Bean
    public ReactiveGridFsTemplate reactiveGridFsTemplate(ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory, MongoConverter mongoConverter) {
        return new ReactiveGridFsTemplate(reactiveMongoDatabaseFactory, mongoConverter);
    }



}
