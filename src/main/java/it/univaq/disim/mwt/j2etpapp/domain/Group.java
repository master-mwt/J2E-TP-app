package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "groups")
public class Group {

    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "groups")
    private Set<User> users;

    @ManyToMany
    private Set<Service> services;

}
