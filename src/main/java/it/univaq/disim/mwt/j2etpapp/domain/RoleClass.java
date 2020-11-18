package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class RoleClass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank
    @Size(max = 50)
    private String name;

    @Column(columnDefinition = "text")
    @Size(max = 200)
    private String description;

    // relations

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<ServiceClass> services;

    @OneToMany(mappedBy = "role")
    private Set<UserChannelRole> userChannelRoles;


    public RoleClass(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleClass roleClass = (RoleClass) o;
        return Objects.equals(name, roleClass.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "RoleClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
