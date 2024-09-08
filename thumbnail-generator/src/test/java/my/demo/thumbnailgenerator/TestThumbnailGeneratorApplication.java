package my.demo.thumbnailgenerator;

import org.springframework.boot.SpringApplication;

public class TestThumbnailGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.from(ThumbnailGeneratorApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
