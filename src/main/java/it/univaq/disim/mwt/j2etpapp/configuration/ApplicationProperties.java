package it.univaq.disim.mwt.j2etpapp.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Data
@Component
@Valid
@ConfigurationProperties(prefix = "postd")
public class ApplicationProperties {
    private String imagesStoragePathAbsolute;
    private String imagesStoragePathRelative;
}
