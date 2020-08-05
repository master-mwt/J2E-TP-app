package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Document(collection = "replies")
public class ReplyClass implements Serializable {

    @Id
    private String id;
    private String content;
    private Long upvote;
    private Long downvote;
    @CreatedDate
    private Date created_at;

    @Version
    private Long version;

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "channel_id")
    private Long channelId;

    @DBRef
    private PostClass post;

    @Column(name = "users_downvoted")
    private Set<Long> usersDownvoted;

    @Column(name = "users_upvoted")
    private Set<Long> usersUpvoted;
}
