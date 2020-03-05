package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;

// TODO: Reply is a mongoDB entity !
@Data
@Document(collection = "replies")
public class Reply implements Serializable {

    @Id
    private Long id;
    private String content;

    @Version
    private Long version;

    @DBRef
    private Post post;
    
    @Column(name = "reply_id")
    private Long replyId;

}
