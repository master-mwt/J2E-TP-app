package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.UserBO;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
