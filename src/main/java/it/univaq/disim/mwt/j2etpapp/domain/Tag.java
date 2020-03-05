package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;

// TODO: Tag is a mongoDB entity !
@Data
@Document(collection = "tags")
public class Tag implements Serializable {

    @Id
    private String id;
    private String name;

    @Version
    private Long version;
    
}
