package my.demo.photoservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.MimeType;

import java.util.Set;

@ConfigurationProperties(prefix = "photo-service")
public record PhotoServiceConfiguration(Set<MimeType> mimeTypes) {
}
