package it.univaq.disim.mwt.postd.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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
@NoArgsConstructor
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

    @NotNull
    private Long userId;

    @NotNull
    private Long channelId;

    private boolean reported;

    private Set<TagClass> tags;

    @DBRef
    private Set<ReplyClass> replies;

    private Set<Long> images;

    private Set<Long> usersDownvoted;

    private Set<Long> usersUpvoted;

    private Set<Long> usersHidden;

    private Set<Long> usersReported;

    private Set<Long> usersSaved;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostClass postClass = (PostClass) o;
        return Objects.equals(id, postClass.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PostClass{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
