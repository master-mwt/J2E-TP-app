package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.UserBO;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserBOImpl implements UserBO {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserClass> findAllUsers() {
        return (List<UserClass>) userRepository.findAll();
    }

    @Override
    public UserClass findUserByID(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserClass findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public void save(UserClass userClass) {
        userRepository.save(userClass);
    }

    @Override
    public void deleteByID(Long id) {
        userRepository.deleteById(id);
    }
}
