package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

// TODO: Check if this relation is correct (User-Role-Channel)
// TODO: JoinColumn is redundant ?
@Data
@Entity
@Table(name = "users_channels_roles")
public class UserChannelRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Embedded
    private UserChannelRoleFKs userChannelRoleFKs;

    @ManyToOne
    @MapsId("user_id")
    //@JoinColumn("user_id")
    private UserClass user;

    @ManyToOne
    @MapsId("role_id")
    //@JoinColumn("role_id")
    private RoleClass role;

    @ManyToOne
    @MapsId("channel_id")
    //@JoinColumn("channel_id")
    private ChannelClass channel;
}
