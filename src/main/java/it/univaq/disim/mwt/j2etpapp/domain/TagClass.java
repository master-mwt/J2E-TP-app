package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "tags")
public class TagClass implements Serializable {

    @Id
    private String id;
    @NotBlank
    private String name;

    @Version
    private Long version;

    public TagClass(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagClass tagClass = (TagClass) o;
        return name.equals(tagClass.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
