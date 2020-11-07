package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.ImageClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;

import java.util.List;

public interface UserBO {

    List<UserClass> findAll();
    UserClass findById(Long id);
    UserClass findByUsername(String username);
    List<UserClass> findByUsernameContains(String username);
    Page<UserClass> findByUsernameContainsPaginated(String username, int page, int size);
    List<UserClass> findByEmailContains(String email);
    Page<UserClass> findByEmailContainsPaginated(String email, int page, int size);
    void save(UserClass user);
    void saveAll(List<UserClass> users);
    void saveAll(UserClass... users);
    void deleteById(Long id);
    void delete(UserClass user);
    Long count();

    void hardBanToggle(Long userId);
    boolean checkOldPassword(UserClass user, String oldPassword);
    void changePassword(UserClass user, String newPassword);
    void removeImage(long userId);
    void saveImage(long userId, ImageClass image);
}
