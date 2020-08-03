package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.UserClass;

import java.util.List;
// TODO: List to Set
public interface UserBO {

    List<UserClass> findAllUsers();
    void save(UserClass userClass);
    UserClass findUserByID(Long id);
    void deleteByID(Long id);

}
