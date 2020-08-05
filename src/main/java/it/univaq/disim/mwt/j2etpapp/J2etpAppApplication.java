package it.univaq.disim.mwt.j2etpapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "it.univaq.disim.mwt.j2etpapp.repository.jpa")
@EnableMongoRepositories(basePackages = "it.univaq.disim.mwt.j2etpapp.repository.mongo")
public class J2etpAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(J2etpAppApplication.class, args);
    }

}
