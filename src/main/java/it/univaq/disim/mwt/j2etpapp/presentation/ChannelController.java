package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.*;
import it.univaq.disim.mwt.j2etpapp.domain.*;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController
@RequestMapping("channel")
public class ChannelController {

    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private UserChannelRoleBO userChannelRoleBO;
    @Autowired
    private RoleBO roleBO;
    @Autowired
    private PostBO postBO;
    @Autowired
    private UserBO userBO;

    @PostMapping("{channelId}/join")
    public ResponseEntity doJoin(@PathVariable("channelId") Long channelId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            RoleClass member = roleBO.findByName("member");
            UserChannelRole joinedMember = new UserChannelRole();
            UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();
            userChannelRoleFKs.setUserId(principal.getId());
            userChannelRoleFKs.setChannelId(channelId);
            userChannelRoleFKs.setRoleId(member.getId());

            joinedMember.setUserChannelRoleFKs(userChannelRoleFKs);

            userChannelRoleBO.save(joinedMember);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{channelId}/leave")
    public ResponseEntity doLeave(@PathVariable("channelId") Long channelId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            userChannelRoleBO.delete(userChannelRoleBO.findByChannelIdAndUserId(channelId, principal.getId()));
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("{channelId}")
    public ResponseEntity doDelete(@PathVariable("channelId") Long channelId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            channelBO.deleteById(channelId);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{channelId}/posts/{postId}/report")
    public ResponseEntity doReportPostInChannel(@PathVariable("channelId") Long channelId, @PathVariable("postId") String postId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            PostClass post = postBO.findById(postId);
            if(!post.getChannelId().equals(channelId)){
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            post.setReported(true);
            postBO.save(post);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{channelId}/members/{userId}/report")
    public ResponseEntity doReportUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            ChannelClass channel = channelBO.findById(channelId);
            if(channel.getReportedUsers() == null){
                channel.setReportedUsers(new HashSet<>());
            }
            channel.getReportedUsers().add(userBO.findById(userId));
            channelBO.save(channel);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{channelId}/members/{userId}/softban")
    public ResponseEntity doSoftBanUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            ChannelClass channel = channelBO.findById(channelId);
            if(channel.getSoftBannedUsers() == null){
                channel.setSoftBannedUsers(new HashSet<>());
            }
            channel.getSoftBannedUsers().add(userBO.findById(userId));
            channelBO.save(channel);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{channelId}/members/{userId}/upgrade")
    public ResponseEntity upgradeMember(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        RoleClass member = roleBO.findByName("member");
        RoleClass moderator = roleBO.findByName("moderator");
        RoleClass admin = roleBO.findByName("admin");
        RoleClass creator = roleBO.findByName("creator");
        if(principal != null){
            UserChannelRole currentMember = userChannelRoleBO.findByChannelIdAndUserId(channelId, userId);

            if(member.equals(currentMember.getRole())){
                currentMember.setRole(moderator);
            } else if(moderator.equals(currentMember.getRole())) {
                currentMember.setRole(admin);
            } else if(admin.equals(currentMember.getRole())) {
                // TODO: admin -> creator ?
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            userChannelRoleBO.save(currentMember);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{channelId}/members/{userId}/downgrade")
    public ResponseEntity downgradeMember(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        RoleClass member = roleBO.findByName("member");
        RoleClass moderator = roleBO.findByName("moderator");
        RoleClass admin = roleBO.findByName("admin");
        RoleClass creator = roleBO.findByName("creator");
        if(principal != null){
            UserChannelRole currentMember = userChannelRoleBO.findByChannelIdAndUserId(channelId, userId);

            if(moderator.equals(currentMember.getRole())){
                currentMember.setRole(member);
            } else if(admin.equals(currentMember.getRole())) {
                currentMember.setRole(moderator);
            } else if(creator.equals(currentMember.getRole())) {
                // TODO: admin <- creator ?
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            userChannelRoleBO.save(currentMember);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }
}
