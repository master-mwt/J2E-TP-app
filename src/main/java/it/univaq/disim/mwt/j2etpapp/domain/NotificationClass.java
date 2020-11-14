package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "notifications")
public class NotificationClass implements Serializable {
    @Id
    private String id;
    private Long userTargetId;
    private Long userCreatedById;

    private String channelName;
    private Long channelId;
    private String postTitle;
    private String postId;

    private String content;
    private String scope;
    @CreatedDate
    private Date created_at;

    @Version
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationClass that = (NotificationClass) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
