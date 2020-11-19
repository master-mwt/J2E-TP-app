package it.univaq.disim.mwt.postd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "it.univaq.disim.mwt.postd.repository.jpa")
@EnableMongoRepositories(basePackages = "it.univaq.disim.mwt.postd.repository.mongo")
@EnableMongoAuditing
public class PostdApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostdApplication.class, args);
    }

}
