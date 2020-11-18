package it.univaq.disim.mwt.postd.helpers;

import it.univaq.disim.mwt.postd.business.*;
import it.univaq.disim.mwt.postd.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PermissionChecker {

    @Autowired
    private UserChannelRoleBO userChannelRoleBO;
    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private ServiceBO serviceBO;
    @Autowired
    private RoleBO roleBO;

    public boolean hasPermissionOnChannel(UserClass currentUser, ChannelClass channel, String permission){
        if(currentUser == null || channel == null || permission == null) {
            return false;
        }

        if("administrator".equals(currentUser.getGroup().getName())) {
            for (ServiceClass service : currentUser.getGroup().getServices()) {
                if(service.getName().equals(permission)) {
                    return true;
                }
            }
            return false;
        }

        UserChannelRole userChannelRole = userChannelRoleBO.findByChannelIdAndUserId(channel.getId(), currentUser.getId());
        ServiceClass joinChannel = serviceBO.findByName("join_channel");

        if(userChannelRole != null) {
            if(joinChannel.getName().equals(permission)) {
                return false;
            }
            RoleClass role = userChannelRole.getRole();
            for (ServiceClass service : role.getServices()) {
                if(service.getName().equals(permission)) {
                    return true;
                }
            }
        } else {
            try {
                if(joinChannel.getName().equals(permission) && !(channelBO.getSoftBannedUsers(channel.getId()).contains(currentUser))) {
                    return true;
                }
            } catch (BusinessException e) {
                log.info("hasPermissionOnChannel: Error in checking permissions on channel with id " + channel.getId());
            }
        }
        return false;
    }

    public boolean hasPermissionOnPost(UserClass currentUser, PostClass post, String permission){
        if(currentUser == null || post == null || permission == null) {
            return false;
        }

        if("administrator".equals(currentUser.getGroup().getName())) {
            for (ServiceClass service : currentUser.getGroup().getServices()) {
                if(service.getName().equals(permission)) {
                    return true;
                }
            }
            return false;
        }

        UserChannelRole userChannelRole = userChannelRoleBO.findByChannelIdAndUserId(post.getChannelId(), currentUser.getId());
        RoleClass creator = roleBO.findByName("creator");
        RoleClass admin = roleBO.findByName("admin");
        RoleClass moderator = roleBO.findByName("moderator");

        if(userChannelRole != null) {
            RoleClass role = userChannelRole.getRole();
            for (ServiceClass service : role.getServices()) {
                if(service.getName().equals(permission) && (post.getUserId().equals(currentUser.getId()) || creator.equals(role) || admin.equals(role) || moderator.equals(role))) {
                    return true;
                }
            }
        } else {
            if(post.getUserId().equals(currentUser.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPermissionOnReply(UserClass currentUser, ReplyClass reply, String permission){
        if(currentUser == null || reply == null || permission == null) {
            return false;
        }

        if("administrator".equals(currentUser.getGroup().getName())) {
            for (ServiceClass service : currentUser.getGroup().getServices()) {
                if(service.getName().equals(permission)) {
                    return true;
                }
            }
            return false;
        }

        UserChannelRole userChannelRole = userChannelRoleBO.findByChannelIdAndUserId(reply.getChannelId(), currentUser.getId());
        RoleClass creator = roleBO.findByName("creator");
        RoleClass admin = roleBO.findByName("admin");
        RoleClass moderator = roleBO.findByName("moderator");

        if(userChannelRole != null) {
            RoleClass role = userChannelRole.getRole();
            for (ServiceClass service : role.getServices()) {
                if(service.getName().equals(permission) && (reply.getUserId().equals(currentUser.getId()) || creator.equals(role) || admin.equals(role) || moderator.equals(role))) {
                    return true;
                }
            }
        } else {
            if(reply.getUserId().equals(currentUser.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPermissionOnUser(UserClass currentUser, UserClass user, String permission){
        ServiceClass hardban_user_from_platform = serviceBO.findByName("hardban_user_from_platform");
        ServiceClass upgrade_user_to_administrator = serviceBO.findByName("upgrade_user_to_administrator");
        ServiceClass downgrade_user_to_logged = serviceBO.findByName("downgrade_user_to_logged");

        if(currentUser == null || user == null || permission == null) {
            return false;
        }

        for (ServiceClass service : currentUser.getGroup().getServices()) {
            if(service.getName().equals(permission) && service.getName().equals(hardban_user_from_platform.getName())) {
                return true;
            } else if(service.getName().equals(permission) && service.getName().equals(upgrade_user_to_administrator.getName())) {
                return true;
            } else if(service.getName().equals(permission) && service.getName().equals(downgrade_user_to_logged.getName())) {
                return true;
            }

            if(service.getName().equals(permission) && currentUser.getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }
}
