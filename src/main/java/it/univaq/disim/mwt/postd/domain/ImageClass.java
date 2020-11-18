package it.univaq.disim.mwt.postd.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "images")
public class ImageClass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text", nullable = false)
    private String type;

    @Column(columnDefinition = "text", nullable = false)
    private String size;

    @Column(columnDefinition = "text", nullable = false)
    @NotBlank
    private String location;

    @Transient
    private boolean first;

    @Column(columnDefinition = "text")
    private String caption;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageClass that = (ImageClass) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(type, that.type) &&
                Objects.equals(size, that.size) &&
                Objects.equals(location, that.location) &&
                Objects.equals(caption, that.caption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, size, location, caption);
    }

    @Override
    public String toString() {
        return "ImageClass{" +
                "id=" + id +
                ", location='" + location + '\'' +
                '}';
    }
}
