package it.univaq.disim.mwt.j2etpapp.security;

import it.univaq.disim.mwt.j2etpapp.business.*;
import it.univaq.disim.mwt.j2etpapp.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
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
    private UserChannelRoleBO userChannelRoleBO;

    @Override
    public boolean hasPermission(Authentication authentication, Object object, Object permission) {
        UserDetailsImpl principal = getPrincipal(authentication);

        if(principal == null){
            return false;
        }

        if("administrator".equals(principal.getUser().getGroup().getName())) {
            return true;
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

        if("administrator".equals(principal.getUser().getGroup().getName())) {
            return true;
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
        UserChannelRole userChannelRole = userChannelRoleBO.findByChannelIdAndUserId(channel.getId(), currentUser.getId());
        if(userChannelRole != null) {
            RoleClass role = userChannelRole.getRole();
            for (ServiceClass service : role.getServices()) {
                if(service.getName().equals(permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasPermissionOnPost(UserClass currentUser, PostClass post, String permission){
        UserChannelRole userChannelRole = userChannelRoleBO.findByChannelIdAndUserId(post.getChannelId(), currentUser.getId());
        if(userChannelRole != null) {
            RoleClass role = userChannelRole.getRole();
            for (ServiceClass service : role.getServices()) {
                if(service.getName().equals(permission) && post.getUserId().equals(currentUser.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasPermissionOnReply(UserClass currentUser, ReplyClass reply, String permission){
        UserChannelRole userChannelRole = userChannelRoleBO.findByChannelIdAndUserId(reply.getChannelId(), currentUser.getId());
        if(userChannelRole != null) {
            RoleClass role = userChannelRole.getRole();
            for (ServiceClass service : role.getServices()) {
                if(service.getName().equals(permission) && reply.getUserId().equals(currentUser.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasPermissionOnUser(UserClass currentUser, UserClass user, String permission){
        for (ServiceClass service : currentUser.getGroup().getServices()) {
            if(service.getName().equals(permission) && currentUser.getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
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
