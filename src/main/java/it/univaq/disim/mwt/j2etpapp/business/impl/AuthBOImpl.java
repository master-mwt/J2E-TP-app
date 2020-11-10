package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.AuthBO;
import it.univaq.disim.mwt.j2etpapp.domain.GroupClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.GroupRepository;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthBOImpl implements AuthBO {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    private static PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void registerUser(UserClass user) {
        GroupClass logged = groupRepository.findByName("logged").orElse(null);

        user.setGroup(logged);
        user.setPassword(encoder.encode(user.getPassword()));

        // user creation
        userRepository.save(user);
    }
}
