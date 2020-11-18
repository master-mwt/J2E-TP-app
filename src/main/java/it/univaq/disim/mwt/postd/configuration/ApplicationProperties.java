package it.univaq.disim.mwt.postd.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Data
@Component
@Valid
@ConfigurationProperties(prefix = "postd")
public class ApplicationProperties {
    private String imagesStoragePath;
    private String dateFormat;
    private Long minPasswordLength;
}
