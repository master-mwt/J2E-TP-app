package it.univaq.disim.mwt.j2etpapp.security;

import it.univaq.disim.mwt.j2etpapp.business.ChannelBO;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;

// TODO: uncomment to activate class
//@Component
public class PermissionEvaluatorImpl implements PermissionEvaluator {

    @Autowired
    private ChannelBO channelBO;

    @Override
    public boolean hasPermission(Authentication authentication, Object object, Object permission) {
        UserDetailsImpl principal = getPrincipal(authentication);

        if(principal == null){
            return false;
        }

        // if-else for all permission-protected classes
        if(object instanceof ChannelClass){
            return hasPermissionOnChannel(principal.getUser(), (ChannelClass) object, (String) permission);
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable objectId, String className, Object permission) {
        UserDetailsImpl principal = getPrincipal(authentication);

        if(principal == null){
            return false;
        }

        // if-else for all permission-protected classes
        if(ChannelClass.class.getName().equals(className)){
            return hasPermissionOnChannel(principal.getUser(), channelBO.findById((Long) objectId), (String) permission);
        }

        return false;
    }
    
    // hasPermission implementations for each protected class
    private boolean hasPermissionOnChannel(UserClass user, ChannelClass channel, String permission){
        throw new NotImplementedException();
    }


    private UserDetailsImpl getPrincipal(Authentication authentication){
        if(authentication.getPrincipal() instanceof UserDetailsImpl){
            return (UserDetailsImpl) authentication.getPrincipal();
        } else {
            // non logged user
            return null;
        }
    }
}
