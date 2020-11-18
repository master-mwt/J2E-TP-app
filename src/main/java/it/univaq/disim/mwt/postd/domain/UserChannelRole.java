package it.univaq.disim.mwt.postd.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users_channels_roles")
public class UserChannelRole implements Serializable {
    @EmbeddedId
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
