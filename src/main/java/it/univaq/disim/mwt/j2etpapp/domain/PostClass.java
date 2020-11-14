package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "posts")
public class PostClass implements Serializable {

    @Id
    private String id;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private Long upvote;
    private Long downvote;
    @CreatedDate
    private Date created_at;

    @Version
    private Long version;

    @Column(name = "user_id")
    @NotNull
    private Long userId;

    @Column(name = "channel_id")
    @NotNull
    private Long channelId;

    private boolean reported;

    private Set<TagClass> tags;

    @DBRef
    private Set<ReplyClass> replies;

    @Column(name = "images")
    private Set<Long> images;

    @Column(name = "users_downvoted")
    private Set<Long> usersDownvoted;

    @Column(name = "users_upvoted")
    private Set<Long> usersUpvoted;

    @Column(name = "users_hidden")
    private Set<Long> usersHidden;

    @Column(name = "users_reported")
    private Set<Long> usersReported;

    @Column(name = "users_saved")
    private Set<Long> usersSaved;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostClass postClass = (PostClass) o;
        return id.equals(postClass.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
