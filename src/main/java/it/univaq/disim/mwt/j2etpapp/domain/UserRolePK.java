package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class UserRolePK implements Serializable {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "role_id")
    private Long roleId;

}
