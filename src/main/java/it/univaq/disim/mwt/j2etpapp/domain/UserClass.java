package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
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
    @NotBlank
    @Email
    private String email;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 30)
    private String name;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 30)
    private String surname;

    @Column(unique = true, nullable = false)
    @NotBlank
    @Size(max = 30)
    private String username;

    @Column(nullable = false)
    @NotBlank
    private String password;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean hard_ban;

    @Column(name = "birth_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserClass userClass = (UserClass) o;
        return id.equals(userClass.id) &&
                username.equals(userClass.username);
    }
}
