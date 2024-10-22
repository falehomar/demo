package my.demo.photoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(PhotoServiceConfiguration.class)
@SpringBootApplication
public class PhotoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoServiceApplication.class, args);
	}


}
