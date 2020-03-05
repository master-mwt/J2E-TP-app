package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

// TODO: Validation in all domain classes
@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    @ManyToOne
    private Group group;

    // This is on MongoDB
    //@OneToMany(mappedBy = "user")
    //private Set<Post> posts;

    @ManyToMany
    private Set<Channel> channels;

    @OneToMany(mappedBy = "user")
    private Set<UserRole> userRole;

}
