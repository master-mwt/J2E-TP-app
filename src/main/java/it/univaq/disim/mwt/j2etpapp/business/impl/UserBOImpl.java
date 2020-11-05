package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.Page;
import it.univaq.disim.mwt.j2etpapp.business.UserBO;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class UserBOImpl implements UserBO {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserClass> findAll() {
        return (List<UserClass>) userRepository.findAll();
    }

    @Override
    public UserClass findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserClass findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public List<UserClass> findByUsernameContains(String username) {
        return userRepository.findByUsernameContains(username).orElse(new ArrayList<>());
    }

    @Override
    public Page<UserClass> findByUsernameContainsPaginated(String username, int page, int size) {
        return new Page<>(userRepository.findByUsernameContains(username, PageRequest.of(page, size)));
    }

    @Override
    public List<UserClass> findByEmailContains(String email) {
        return userRepository.findByEmailContains(email).orElse(new ArrayList<>());
    }

    @Override
    public Page<UserClass> findByEmailContainsPaginated(String email, int page, int size) {
        return new Page<>(userRepository.findByEmailContains(email, PageRequest.of(page, size)));
    }

    @Override
    public void save(UserClass user) {
        userRepository.save(user);
    }

    @Override
    public void saveAll(List<UserClass> users) {
        userRepository.saveAll(users);
    }

    @Override
    public void saveAll(UserClass... users) {
        userRepository.saveAll(Arrays.asList(users));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void delete(UserClass user) {
        userRepository.delete(user);
    }

    @Override
    public Long count() {
        return userRepository.count();
    }
    
    @Override
    public void hardBanToggle(Long userId) {
        UserClass user = userRepository.findById(userId).orElse(null);
        if(user.isHard_ban()){
            user.setHard_ban(false);
        } else {
            user.setHard_ban(true);
        }
        userRepository.save(user);
    }
}
