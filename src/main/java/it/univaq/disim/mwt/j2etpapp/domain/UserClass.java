package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

// TODO: Validation in all domain classes
@Getter
@Setter
@RequiredArgsConstructor
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
    private String surname;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean hard_ban;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamp default current_timestamp")
    private Date createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<UserChannelRole> userChannelRoles;

    @ManyToMany(mappedBy = "softBannedUsers")
    private Set<ChannelClass> softBannedIn;

    @ManyToMany(mappedBy = "reportedUsers")
    private Set<ChannelClass> reportedIn;

    @ManyToOne(optional = false)
    private GroupClass group;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private ImageClass image;
}
