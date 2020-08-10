package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "roles")
public class RoleClass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToMany
    private Set<ServiceClass> services;

    @OneToMany(mappedBy = "role")
    private Set<UserChannelRole> userChannelRoles;
}
