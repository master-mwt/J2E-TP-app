package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.UserClass;

import java.util.List;
// TODO: List to Set
public interface UserBO {

    List<UserClass> findAllUsers();
    UserClass findUserByID(Long id);
    UserClass findUserByUsername(String username);
    void save(UserClass userClass);
    void deleteByID(Long id);

}
