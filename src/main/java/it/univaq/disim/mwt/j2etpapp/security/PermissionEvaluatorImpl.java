package it.univaq.disim.mwt.j2etpapp.security;

import it.univaq.disim.mwt.j2etpapp.business.*;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.ReplyClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.helpers.PermissionChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

// TODO: permission correct check
@Component
@Slf4j
public class PermissionEvaluatorImpl implements PermissionEvaluator {

    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private PostBO postBO;
    @Autowired
    private ReplyBO replyBO;
    @Autowired
    private UserBO userBO;

    @Autowired
    private PermissionChecker permissionChecker;

    @Override
    public boolean hasPermission(Authentication authentication, Object object, Object permission) {
        UserDetailsImpl principal = getPrincipal(authentication);

        if(principal == null){
            return false;
        }

        /*if("administrator".equals(principal.getUser().getGroup().getName())) {
            return true;
        }*/

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
    public boolean hasPermission(Authentication authentication, Serializable objectId, String objectClassName, Object permission) {
        UserDetailsImpl principal = getPrincipal(authentication);

        if(principal == null){
            return false;
        }

        /*if("administrator".equals(principal.getUser().getGroup().getName())) {
            return true;
        }*/

        // if-else for all permission-protected classes
        try {
            if(ChannelClass.class.getName().equals(objectClassName)){
                return hasPermissionOnChannel(principal.getUser(), channelBO.findById((Long) objectId), (String) permission);
            } else if(PostClass.class.getName().equals(objectClassName)) {
                return hasPermissionOnPost(principal.getUser(), postBO.findById((String) objectId), (String) permission);
            } else if(ReplyClass.class.getName().equals(objectClassName)) {
                return hasPermissionOnReply(principal.getUser(), replyBO.findById((String) objectId), (String) permission);
            } else if(UserClass.class.getName().equals(objectClassName)) {
                return hasPermissionOnUser(principal.getUser(), userBO.findById((Long) objectId), (String) permission);
            }
        } catch (BusinessException e) {
            log.info("hasPermission: Error in check permissions");
        }

        return false;
    }
    
    // hasPermission implementations for each protected class
    private boolean hasPermissionOnChannel(UserClass currentUser, ChannelClass channel, String permission){
        return permissionChecker.hasPermissionOnChannel(currentUser, channel, permission);
    }

    private boolean hasPermissionOnPost(UserClass currentUser, PostClass post, String permission){
        return permissionChecker.hasPermissionOnPost(currentUser, post, permission);
    }

    private boolean hasPermissionOnReply(UserClass currentUser, ReplyClass reply, String permission){
        return permissionChecker.hasPermissionOnReply(currentUser, reply, permission);
    }

    private boolean hasPermissionOnUser(UserClass currentUser, UserClass user, String permission){
        return permissionChecker.hasPermissionOnUser(currentUser, user, permission);
    }


    // get principal
    private UserDetailsImpl getPrincipal(Authentication authentication){
        if(authentication.getPrincipal() instanceof UserDetailsImpl){
            return (UserDetailsImpl) authentication.getPrincipal();
        } else {
            // non logged user
            return null;
        }
    }
}
