package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "channels")
public class ChannelClass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = true)
    private String title;

    @Column(nullable = true, columnDefinition = "text")
    private String description;

    @Column(nullable = true, columnDefinition = "text")
    private String rules;

    //@ManyToMany(mappedBy = "channels")
    //private Set<User> users;

    // This is on MongoDB
    //@OneToMany(mappedBy = "channel")
    //private Set<Post> posts;

    @OneToMany(mappedBy = "channel")
    private Set<UserChannelRole> userChannelRole;

}
