package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.UserClass;

import java.util.List;

public interface UserBO {

    List<UserClass> findAll();
    UserClass findById(Long id);
    UserClass findByUsername(String username);
    void save(UserClass user);
    void saveAll(List<UserClass> users);
    void saveAll(UserClass... users);
    void deleteById(Long id);
    void delete(UserClass user);
    Long count();

}
