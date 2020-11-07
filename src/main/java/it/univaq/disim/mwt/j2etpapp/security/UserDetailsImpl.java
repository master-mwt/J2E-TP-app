package it.univaq.disim.mwt.j2etpapp.security;

import it.univaq.disim.mwt.j2etpapp.domain.ServiceClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private static String ROLE_PREFIX = "ROLE_";
    private UserClass user;

    public UserDetailsImpl(UserClass user) {
        this.user = user;
    }

    public UserClass getUser() {
        return this.user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if(user.getGroup().getName().equals("administrator")){
            authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + user.getGroup().getName()));
        } else if(user.getGroup().getName().equals("logged")){
            authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + user.getGroup().getName()));
        } else {
            return null;
        }
        
        for(ServiceClass service : user.getGroup().getServices()){
            authorities.add(new SimpleGrantedAuthority(service.getName()));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !user.isHard_ban();
    }

    @Override
    public String toString() {
        return "UserDetailsImpl[ username: " + user.getUsername() + " ]";
    }
}
