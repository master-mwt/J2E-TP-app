package it.univaq.disim.mwt.j2etpapp.utils;

import it.univaq.disim.mwt.j2etpapp.business.UserChannelRoleBO;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserChannelRole;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UtilsClass {

    @Autowired
    private UserChannelRoleBO userChannelRoleBO;

    public UserClass getPrincipal() {
        return (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
    }

    public UserChannelRole getSubscription(ChannelClass channel, UserClass principal) {
        return (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? userChannelRoleBO.findByChannelIdAndUserId(channel.getId(), principal.getId()) : null;
    }
}
