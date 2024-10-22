package my.demo.photoservice;

import my.demo.common.Photo;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Collections;

@Configuration
@EnableKafka
@EnableAutoConfiguration(exclude = {MetricsAutoConfiguration.class})
public class MessagingConfiguration {
    @Bean
    public KafkaTemplate<String, Photo> kafkaTemplate(ProducerFactory<String, Photo> producerFactory) {
        return new KafkaTemplate<>(producerFactory,
                Collections.singletonMap(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class));
    }
}
