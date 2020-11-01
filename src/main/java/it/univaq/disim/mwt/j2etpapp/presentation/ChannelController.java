package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.*;
import it.univaq.disim.mwt.j2etpapp.domain.*;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashSet;

//TODO: MISSING LOGGING FEATURE IN ALL CLASSES!!
@RestController
@RequestMapping("channels")
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

    // TODO: is it ok to keep here this functions
    @GetMapping("create")
    public ModelAndView create() {
        // TODO: create channel
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("channel", new ChannelClass());
        modelAndView.setViewName("channel/form");
        return modelAndView;
    }

    @PostMapping("create")
    public ModelAndView save(@Valid @ModelAttribute("channel") ChannelClass channel, Errors errors) {
        // TODO: save channel (is ok ?)
        ModelAndView modelAndView = new ModelAndView();

        if(errors.hasErrors()) {
            modelAndView.addObject("errors", errors.getAllErrors());
            modelAndView.setViewName("channel/form");
        }
        channelBO.save(channel);

        modelAndView.addObject("post", channel);
        modelAndView.setViewName("redirect:/channel/view");
        return modelAndView;
    }

    @DeleteMapping("{channelId}")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'delete_channel')")
    public ModelAndView delete(@PathVariable("channelId") Long channelId) {
        channelBO.deleteById(channelId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @GetMapping("{channelId}/join")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'join_channel')")
    public ModelAndView doJoin(@PathVariable("channelId") Long channelId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        RoleClass member = roleBO.findByName("member");
        UserChannelRole joinedMember = new UserChannelRole();
        UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();
        userChannelRoleFKs.setUserId(principal.getId());
        userChannelRoleFKs.setChannelId(channelId);
        userChannelRoleFKs.setRoleId(member.getId());

        joinedMember.setUserChannelRoleFKs(userChannelRoleFKs);

        userChannelRoleBO.save(joinedMember);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/discover/channel/" + channelId);
        return modelAndView;
    }

    @GetMapping("{channelId}/leave")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'leave_channel')")
    public ModelAndView doLeave(@PathVariable("channelId") Long channelId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        userChannelRoleBO.delete(userChannelRoleBO.findByChannelIdAndUserId(channelId, principal.getId()));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/discover/channel/" + channelId);

        return modelAndView;
    }

    @PostMapping("{channelId}/posts/{postId}/report")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'report_post_in_channel')")
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

    @PostMapping("{channelId}/posts/{postId}/unreport")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'report_post_in_channel')")
    public ResponseEntity doUnReportPostInChannel(@PathVariable("channelId") Long channelId, @PathVariable("postId") String postId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            PostClass post = postBO.findById(postId);
            if(!post.getChannelId().equals(channelId)){
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            post.setReported(false);
            postBO.save(post);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{channelId}/members/{userId}/report")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'report_user_in_channel')")
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

    @PostMapping("{channelId}/members/{userId}/unreport")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'report_user_in_channel')")
    public ResponseEntity doUnReportUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            ChannelClass channel = channelBO.findById(channelId);
            if(channel.getReportedUsers() == null){
                channel.setReportedUsers(new HashSet<>());
            }
            channel.getReportedUsers().remove(userBO.findById(userId));
            channelBO.save(channel);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{channelId}/members/{userId}/softban")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'softban_user_in_channel')")
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

    @PostMapping("{channelId}/members/{userId}/unsoftban")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'softban_user_in_channel')")
    public ResponseEntity doUnSoftBanUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            ChannelClass channel = channelBO.findById(channelId);
            if(channel.getSoftBannedUsers() == null){
                channel.setSoftBannedUsers(new HashSet<>());
            }
            channel.getSoftBannedUsers().remove(userBO.findById(userId));
            channelBO.save(channel);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{channelId}/members/{userId}/upgrade")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'upgrade_users_in_channel')")
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
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'downgrade_users_in_channel')")
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
