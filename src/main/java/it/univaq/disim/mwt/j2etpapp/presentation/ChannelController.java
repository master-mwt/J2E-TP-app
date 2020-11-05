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

    @PostMapping("{channelId}/posts/{postId}/globalunreport")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'global_unreport_post_in_channel')")
    public ResponseEntity doGlobalUnReportPostInChannel(@PathVariable("channelId") Long channelId, @PathVariable("postId") String postId) {
        PostClass post = postBO.findById(postId);
        if(!post.getChannelId().equals(channelId)){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        post.getUsersReported().clear();
        post.setReported(false);
        postBO.save(post);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("{channelId}/members/{userId}/report")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'report_user_in_channel')")
    public ResponseEntity doReportUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        ChannelClass channel = channelBO.findById(channelId);
        if(channel.getReportedUsers() == null){
            channel.setReportedUsers(new HashSet<>());
        }
        channel.getReportedUsers().add(userBO.findById(userId));
        channelBO.save(channel);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("{channelId}/members/{userId}/unreport")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'report_user_in_channel')")
    public ResponseEntity doUnReportUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        ChannelClass channel = channelBO.findById(channelId);
        if(channel.getReportedUsers() == null){
            channel.setReportedUsers(new HashSet<>());
        }
        channel.getReportedUsers().remove(userBO.findById(userId));
        channelBO.save(channel);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("{channelId}/members/{userId}/softban")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'softban_user_in_channel')")
    public ResponseEntity doSoftBanUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        ChannelClass channel = channelBO.findById(channelId);
        if(channel.getSoftBannedUsers() == null){
            channel.setSoftBannedUsers(new HashSet<>());
        }
        channel.getSoftBannedUsers().add(userBO.findById(userId));
        channelBO.save(channel);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("{channelId}/members/{userId}/unsoftban")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'softban_user_in_channel')")
    public ResponseEntity doUnSoftBanUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        ChannelClass channel = channelBO.findById(channelId);
        if(channel.getSoftBannedUsers() == null){
            channel.setSoftBannedUsers(new HashSet<>());
        }
        channel.getSoftBannedUsers().remove(userBO.findById(userId));
        channelBO.save(channel);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("{channelId}/members/{userId}/upgrade_member")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'upgrade_member_to_moderator_in_channel')")
    public ResponseEntity upgradeMemberToModerator(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        RoleClass member = roleBO.findByName("member");
        RoleClass moderator = roleBO.findByName("moderator");

        UserChannelRole currentMember = userChannelRoleBO.findByChannelIdAndUserId(channelId, userId);
        if(member.equals(currentMember.getRole())){
            currentMember.setRole(moderator);
            userChannelRoleBO.save(currentMember);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("{channelId}/members/{userId}/upgrade_moderator")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'upgrade_moderator_to_admin_in_channel')")
    public ResponseEntity upgradeModeratorToAdmin(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        RoleClass moderator = roleBO.findByName("moderator");
        RoleClass admin = roleBO.findByName("admin");

        UserChannelRole currentMember = userChannelRoleBO.findByChannelIdAndUserId(channelId, userId);
        if(moderator.equals(currentMember.getRole())){
            currentMember.setRole(admin);
            userChannelRoleBO.save(currentMember);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("{channelId}/members/{userId}/downgrade_moderator")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'downgrade_moderator_to_member_in_channel')")
    public ResponseEntity downgradeModeratorToMember(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        RoleClass member = roleBO.findByName("member");
        RoleClass moderator = roleBO.findByName("moderator");

        UserChannelRole currentMember = userChannelRoleBO.findByChannelIdAndUserId(channelId, userId);
        if(moderator.equals(currentMember.getRole())){
            currentMember.setRole(member);
            userChannelRoleBO.save(currentMember);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("{channelId}/members/{userId}/upgrade_admin")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'upgrade_admin_to_creator_in_channel')")
    public ResponseEntity upgradeAdminToCreator(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        RoleClass admin = roleBO.findByName("admin");
        RoleClass creator = roleBO.findByName("creator");
        ChannelClass channel = channelBO.findById(channelId);

        for(UserChannelRole userChannelRole : userChannelRoleBO.findByChannelId(channelId)) {
            if(creator.equals(userChannelRole.getRole())) {
                return new ResponseEntity("Remove creator first", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        UserChannelRole currentMember = userChannelRoleBO.findByChannelIdAndUserId(channelId, userId);
        if(admin.equals(currentMember.getRole())){
            currentMember.setRole(creator);
            channel.setCreator(userBO.findById(userId));
            userChannelRoleBO.save(currentMember);
            channelBO.save(channel);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("{channelId}/members/{userId}/downgrade_admin")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'downgrade_admin_to_moderator_in_channel')")
    public ResponseEntity downgradeAdminToModerator(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        RoleClass moderator = roleBO.findByName("moderator");
        RoleClass admin = roleBO.findByName("admin");

        UserChannelRole currentMember = userChannelRoleBO.findByChannelIdAndUserId(channelId, userId);
        if(admin.equals(currentMember.getRole())){
            currentMember.setRole(moderator);
            userChannelRoleBO.save(currentMember);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("{channelId}/members/{userId}/downgrade_creator")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'downgrade_creator_to_admin_in_channel')")
    public ResponseEntity downgradeCreatorToAdmin(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        RoleClass admin = roleBO.findByName("admin");
        RoleClass creator = roleBO.findByName("creator");

        UserChannelRole currentMember = userChannelRoleBO.findByChannelIdAndUserId(channelId, userId);
        if(creator.equals(currentMember.getRole())){
            currentMember.setRole(admin);
            userChannelRoleBO.save(currentMember);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
