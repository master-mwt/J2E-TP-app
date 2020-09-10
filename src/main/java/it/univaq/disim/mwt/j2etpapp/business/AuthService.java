package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.GroupClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
// TODO: is it okay that this class is a Service ?
public class AuthService {

    private static PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserBO userBO;
    @Autowired
    private GroupBO groupBO;

    public void performRegistration(UserClass user) throws BusinessException {

        GroupClass logged = groupBO.findByName("logged");

        if(logged == null){
            throw new BusinessException();
        }

        user.setGroup(logged);
        user.setPassword(encoder.encode(user.getPassword()));

        // user creation
        userBO.save(user);
    }
}
