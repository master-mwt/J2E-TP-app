package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

// TODO: Validation in all domain classes
@Data
@Entity
@Table(name = "users")
public class UserClass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    //@ManyToOne
    //private Group group;

    // This is on MongoDB
    //@OneToMany(mappedBy = "user")
    //private Set<Post> posts;

    //@ManyToMany
    //private Set<Channel> channels;

    @OneToMany(mappedBy = "userClass")
    private Set<UserChannelRole> userChannelRole;

}
