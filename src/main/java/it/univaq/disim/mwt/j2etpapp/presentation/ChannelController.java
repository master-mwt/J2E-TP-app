package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.BusinessException;
import it.univaq.disim.mwt.j2etpapp.business.ChannelBO;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

//TODO: MISSING LOGGING FEATURE IN ALL CLASSES!!
@RestController
@RequestMapping("channels")
public class ChannelController {

    @Autowired
    private ChannelBO channelBO;

    @PostMapping("create")
    @PreAuthorize("hasAuthority('create_channel')")
    public ModelAndView save(@Valid @ModelAttribute("channel") ChannelClass channel) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        // TODO: save channel (is ok ?), Errors ?
        ModelAndView modelAndView = new ModelAndView();

        channelBO.createChannel(channel, principal);

        modelAndView.setViewName("redirect:/discover/channel/" + channel.getId());
        return modelAndView;
    }

    @PostMapping("{channelId}")
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

        channelBO.joinChannel(channelId, principal);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/discover/channel/" + channelId);
        return modelAndView;
    }

    @GetMapping("{channelId}/leave")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'leave_channel')")
    public ModelAndView doLeave(@PathVariable("channelId") Long channelId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        channelBO.leaveChannel(channelId, principal);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/discover/channel/" + channelId);

        return modelAndView;
    }

    @GetMapping("{channelId}/posts/{postId}/globalunreport")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'global_unreport_post_in_channel')")
    public ModelAndView doGlobalUnReportPostInChannel(@PathVariable("channelId") Long channelId, @PathVariable("postId") String postId) throws BusinessException {
        channelBO.globalUnreportPost(channelId, postId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/discover/channel/" + channelId + "/posts/reported");

        return modelAndView;
    }

    @GetMapping("{channelId}/members/{userId}/report")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'report_user_in_channel')")
    public ModelAndView doReportUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.reportUser(channelId, userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/discover/channel/" + channelId + "/members");

        return modelAndView;
    }

    @GetMapping("{channelId}/members/{userId}/unreport")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'report_user_in_channel')")
    public ModelAndView doUnReportUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.unReportUser(channelId, userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/discover/channel/" + channelId + "/members");

        return modelAndView;
    }

    @GetMapping("{channelId}/members/{userId}/softban")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'softban_user_in_channel')")
    public ModelAndView doSoftBanUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.softBan(channelId, userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/discover/channel/" + channelId + "/members");

        return modelAndView;
    }

    @GetMapping("{channelId}/members/{userId}/unsoftban")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'softban_user_in_channel')")
    public ModelAndView doUnSoftBanUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.unSoftBan(channelId, userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/discover/channel/" + channelId + "/members/banned");

        return modelAndView;
    }

    @GetMapping("{channelId}/members/{userId}/upgrade_member")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'upgrade_member_to_moderator_in_channel')")
    public ModelAndView upgradeMemberToModerator(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.upgradeMemberToModerator(channelId, userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/discover/channel/" + channelId + "/members");

        return modelAndView;
    }

    @GetMapping("{channelId}/members/{userId}/upgrade_moderator")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'upgrade_moderator_to_admin_in_channel')")
    public ModelAndView upgradeModeratorToAdmin(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.upgradeModeratorToAdmin(channelId, userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/discover/channel/" + channelId + "/members");

        return modelAndView;
    }

    @GetMapping("{channelId}/members/{userId}/downgrade_moderator")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'downgrade_moderator_to_member_in_channel')")
    public ModelAndView downgradeModeratorToMember(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.downgradeModeratorToMember(channelId, userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/discover/channel/" + channelId + "/members");

        return modelAndView;
    }

    @GetMapping("{channelId}/members/{userId}/upgrade_admin")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'upgrade_admin_to_creator_in_channel')")
    public ModelAndView upgradeAdminToCreator(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) throws BusinessException {
        channelBO.upgradeAdminToCreator(channelId, userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/discover/channel/" + channelId + "/members");

        return modelAndView;
    }

    @GetMapping("{channelId}/members/{userId}/downgrade_admin")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'downgrade_admin_to_moderator_in_channel')")
    public ModelAndView downgradeAdminToModerator(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.downgradeAdminToModerator(channelId, userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/discover/channel/" + channelId + "/members");

        return modelAndView;
    }

    @GetMapping("{channelId}/members/{userId}/downgrade_creator")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'downgrade_creator_to_admin_in_channel')")
    public ModelAndView downgradeCreatorToAdmin(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.downgradeCreatorToAdmin(channelId, userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/discover/channel/" + channelId + "/members");

        return modelAndView;
    }

    @PostMapping("{channelId}/remove_image")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'mod_channel_data')")
    public ModelAndView removeImage(@PathVariable("channelId") Long channelId) {
        channelBO.removeImage(channelId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/discover/channel/" + channelId);

        return modelAndView;
    }
}
