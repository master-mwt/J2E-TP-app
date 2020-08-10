package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

// TODO: Check if this relation is correct (User-Role-Channel)
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "users_channels_roles")
public class UserChannelRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Embedded
    private UserChannelRoleFKs userChannelRoleFKs;

    @ManyToOne(optional = false)
    @MapsId("user_id")
    private UserClass user;

    @ManyToOne(optional = false)
    @MapsId("role_id")
    private RoleClass role;

    @ManyToOne(optional = false)
    @MapsId("channel_id")
    private ChannelClass channel;
}
