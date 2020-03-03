package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.User;

import java.util.List;
// TODO: List to Set
public interface UserBO {

    List<User> findAllUsers();
    void save(User user);
    User findUserByID(Long id);
    void deleteByID(Long id);

}
