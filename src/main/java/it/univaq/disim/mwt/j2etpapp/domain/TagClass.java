package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "tags")
public class TagClass implements Serializable {

    @Id
    private String id;
    private String name;

    @Version
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagClass tagClass = (TagClass) o;
        return name.equals(tagClass.name);
    }
}
