package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.UserBO;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public void save(UserClass userClass) {
        userRepository.save(userClass);
    }

    @Override
    public UserClass findUserByID(Long id) {
        Optional<UserClass> result = userRepository.findById(id);

        return result.orElse(null);
    }

    @Override
    public void deleteByID(Long id) {
        userRepository.deleteById(id);
    }
}
