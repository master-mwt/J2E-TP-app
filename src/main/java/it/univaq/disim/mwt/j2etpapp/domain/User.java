package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;

    @ManyToOne
    private Group group;

}
