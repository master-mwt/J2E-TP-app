/*
package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

// TODO: Post is a mongoDB entity !
// TODO: How to do cross-database ?
@Data
@Document(collection = "posts")
public class Post implements Serializable {

    @Id
    private String id;
    private String title;
    private String content;

    @Version
    private Long version;

    // This is on MariaDB
    //@ManyToOne
    //private User user;
    @Column(name = "user_id")
    private Long userId;

    // This is on MariaDB
    //@ManyToOne
    //private Channel channel;
    @Column(name = "channel_id")
    private Long channelId;

    //@ManyToMany
    //@DBRef
    private Set<Tag> tags;

    //@OneToMany
    @DBRef
    private Set<Reply> replies;

}
*/
