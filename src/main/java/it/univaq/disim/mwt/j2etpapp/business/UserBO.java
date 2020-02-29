package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.User;

import java.util.List;

public interface UserBO {

    List<User> findAllUsers();

    void save(User user);

}
