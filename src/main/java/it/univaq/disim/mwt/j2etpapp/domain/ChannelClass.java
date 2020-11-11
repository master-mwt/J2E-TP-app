package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "channels")

public class ChannelClass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank
    @Size(max = 30)
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
