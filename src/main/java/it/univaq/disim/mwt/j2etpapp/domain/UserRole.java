package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

// TODO: Check if this relation is correct (User-Role-Channel)
// TODO: JoinColumn is redundant ?
@Data
@Entity
@Table(name = "users_roles")
public class UserRole implements Serializable {

    @EmbeddedId
    private UserRolePK userRolePK;

    @ManyToOne
    @MapsId("user_id")
    //@JoinColumn("user_id")
    private User user;

    @ManyToOne
    @MapsId("role_id")
    //@JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    private Channel channel;

}
