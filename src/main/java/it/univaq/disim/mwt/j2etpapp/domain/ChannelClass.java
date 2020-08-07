package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
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

    @Column()
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String rules;

    @OneToOne(cascade = CascadeType.REMOVE)
    private ImageClass image;

    @OneToOne
    private UserClass creator;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamp default current_timestamp")
    private Date createdAt;

    //@UpdateTimestamp
    //@Temporal(TemporalType.TIMESTAMP)
    //@Column(name = "updated_at", nullable = false, columnDefinition = "timestamp")
    //private Date updatedAt = new Date();

    @OneToMany(mappedBy = "channel", cascade = CascadeType.REMOVE)
    private Set<UserChannelRole> userChannelRoles;

    @ManyToMany
    private Set<UserClass> softBannedUsers;

    @ManyToMany
    private Set<UserClass> reportedUsers;
}
