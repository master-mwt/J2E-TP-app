package it.univaq.disim.mwt.postd.security;

import it.univaq.disim.mwt.postd.business.UserBO;
import it.univaq.disim.mwt.postd.domain.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserBO userBO;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserClass user = userBO.findByUsername(s);
        if(user == null){
            throw new UsernameNotFoundException("User '" + s + "' not found");
        }

        return new UserDetailsImpl(user);
    }
}
