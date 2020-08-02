package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "groupst")
public class Group implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    
    //@OneToMany(mappedBy = "group")
    //private Set<User> users;

    @ManyToMany
    private Set<Service> services;

}
