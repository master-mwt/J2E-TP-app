package it.univaq.disim.mwt.j2etpapp.security;

import it.univaq.disim.mwt.j2etpapp.business.ChannelBO;
import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.business.ReplyBO;
import it.univaq.disim.mwt.j2etpapp.business.UserBO;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.ReplyClass;
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
    @Autowired
    private PostBO postBO;
    @Autowired
    private ReplyBO replyBO;
    @Autowired
    private UserBO userBO;

    @Override
    public boolean hasPermission(Authentication authentication, Object object, Object permission) {
        UserDetailsImpl principal = getPrincipal(authentication);

        if(principal == null){
            return false;
        }

        // if-else for all permission-protected classes
        if(object instanceof ChannelClass){
            return hasPermissionOnChannel(principal.getUser(), (ChannelClass) object, (String) permission);
        } else if(object instanceof PostClass) {
            return hasPermissionOnPost(principal.getUser(), (PostClass) object, (String) permission);
        } else if(object instanceof ReplyClass) {
            return hasPermissionOnReply(principal.getUser(), (ReplyClass) object, (String) permission);
        } else if(object instanceof UserClass) {
            return hasPermissionOnUser(principal.getUser(), (UserClass) object, (String) permission);
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
        } else if(PostClass.class.getName().equals(className)) {
            return hasPermissionOnPost(principal.getUser(), postBO.findById((String) objectId), (String) permission);
        } else if(ReplyClass.class.getName().equals(className)) {
            return hasPermissionOnReply(principal.getUser(), replyBO.findById((String) objectId), (String) permission);
        } else if(UserClass.class.getName().equals(className)) {
            return hasPermissionOnUser(principal.getUser(), userBO.findById((Long) objectId), (String) permission);
        }

        return false;
    }
    
    // hasPermission implementations for each protected class
    private boolean hasPermissionOnChannel(UserClass currentUser, ChannelClass channel, String permission){
        // TODO: check if current user can modify object
        throw new NotImplementedException();
    }

    private boolean hasPermissionOnPost(UserClass currentUser, PostClass post, String permission){
        // TODO: check if current user can modify object
        throw new NotImplementedException();
    }

    private boolean hasPermissionOnReply(UserClass currentUser, ReplyClass reply, String permission){
        // TODO: check if current user can modify object
        throw new NotImplementedException();
    }

    private boolean hasPermissionOnUser(UserClass currentUser, UserClass user, String permission){
        // TODO: check if current user can modify object
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
