package it.univaq.disim.mwt.j2etpapp.utils;

import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

public class UtilsClass {
    public static UserClass getPrincipal() {
        return (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
    }
}
