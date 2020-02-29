package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Data
@Entity
@Table(name = "services")
public class Service {

    @Id
    private Long id;
    private String name;

    @ManyToMany
    private Set<Group> groups;

}
