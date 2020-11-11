package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.BusinessException;
import it.univaq.disim.mwt.j2etpapp.business.ChannelBO;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

//TODO: MISSING LOGGING FEATURE IN ALL CLASSES!!
@Controller
@RequestMapping("channels")
public class ChannelController {

    @Autowired
    private ChannelBO channelBO;

    @PostMapping("create")
    @PreAuthorize("hasAuthority('create_channel')")
    public String save(@Valid @ModelAttribute("channel") ChannelClass channel, Errors errors) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        if(errors.hasErrors()){
            // TODO: trovare un modo per far vedere errori
            return "";
        }

        channelBO.createChannel(channel, principal);

        return "redirect:/discover/channel/" + channel.getId();
    }

    @PostMapping("{channelId}")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'delete_channel')")
    public String delete(@PathVariable("channelId") Long channelId) {
        channelBO.deleteById(channelId);

        return "redirect:/";
    }

    @PostMapping("{channelId}/update")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'mod_channel_data')")
    public String update(@ModelAttribute("channel") ChannelClass channel, @PathVariable("channelId") Long channelId, Errors errors) {

        if(errors.hasErrors()){
            // TODO: trovare un modo per far vedere errori
            return "";
        }

        channelBO.updateChannel(channelId, channel);

        return "redirect:/discover/channel/" + channelId;
    }

    @GetMapping("{channelId}/join")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'join_channel')")
    public String doJoin(@Valid @PathVariable("channelId") Long channelId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        channelBO.joinChannel(channelId, principal);

        return "redirect:/discover/channel/" + channelId;
    }

    @GetMapping("{channelId}/leave")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'leave_channel')")
    public String doLeave(@PathVariable("channelId") Long channelId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        channelBO.leaveChannel(channelId, principal);

        return "redirect:/discover/channel/" + channelId;
    }

    @GetMapping("{channelId}/posts/{postId}/globalunreport")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'global_unreport_post_in_channel')")
    public String doGlobalUnReportPostInChannel(@PathVariable("channelId") Long channelId, @PathVariable("postId") String postId) throws BusinessException {
        channelBO.globalUnreportPost(channelId, postId);

        return "redirect:/discover/channel/" + channelId + "/posts/reported";
    }

    @GetMapping("{channelId}/members/{userId}/report")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'report_user_in_channel')")
    public String doReportUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.reportUser(channelId, userId);

        return "redirect:/discover/channel/" + channelId + "/members";
    }

    @GetMapping("{channelId}/members/{userId}/unreport")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'report_user_in_channel')")
    public String doUnReportUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.unReportUser(channelId, userId);

        return "redirect:/discover/channel/" + channelId + "/members";
    }

    @GetMapping("{channelId}/members/{userId}/softban")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'softban_user_in_channel')")
    public String doSoftBanUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.softBan(channelId, userId);

        return "redirect:/discover/channel/" + channelId + "/members";
    }

    @GetMapping("{channelId}/members/{userId}/unsoftban")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'softban_user_in_channel')")
    public String doUnSoftBanUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.unSoftBan(channelId, userId);

        return "redirect:/discover/channel/" + channelId + "/members/banned";
    }

    @GetMapping("{channelId}/members/{userId}/upgrade_member")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'upgrade_member_to_moderator_in_channel')")
    public String upgradeMemberToModerator(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.upgradeMemberToModerator(channelId, userId);

        return "redirect:/discover/channel/" + channelId + "/members";
    }

    @GetMapping("{channelId}/members/{userId}/upgrade_moderator")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'upgrade_moderator_to_admin_in_channel')")
    public String upgradeModeratorToAdmin(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.upgradeModeratorToAdmin(channelId, userId);

        return "redirect:/discover/channel/" + channelId + "/members";
    }

    @GetMapping("{channelId}/members/{userId}/downgrade_moderator")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'downgrade_moderator_to_member_in_channel')")
    public String downgradeModeratorToMember(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.downgradeModeratorToMember(channelId, userId);

        return "redirect:/discover/channel/" + channelId + "/members";
    }

    @GetMapping("{channelId}/members/{userId}/upgrade_admin")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'upgrade_admin_to_creator_in_channel')")
    public String upgradeAdminToCreator(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) throws BusinessException {
        channelBO.upgradeAdminToCreator(channelId, userId);

        return "redirect:/discover/channel/" + channelId + "/members";
    }

    @GetMapping("{channelId}/members/{userId}/downgrade_admin")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'downgrade_admin_to_moderator_in_channel')")
    public String downgradeAdminToModerator(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.downgradeAdminToModerator(channelId, userId);

        return "redirect:/discover/channel/" + channelId + "/members";
    }

    @GetMapping("{channelId}/members/{userId}/downgrade_creator")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'downgrade_creator_to_admin_in_channel')")
    public String downgradeCreatorToAdmin(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) {
        channelBO.downgradeCreatorToAdmin(channelId, userId);

        return "redirect:/discover/channel/" + channelId + "/members";
    }

    @GetMapping("{channelId}/change_image")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'mod_channel_data')")
    public String changeImage(@PathVariable("channelId") Long channelId, Model model) {
        model.addAttribute("channel", channelBO.findById(channelId));

        return "pages/dashboard/image_upload/channel_img_upl";
    }

    @PostMapping("{channelId}/change_image")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'mod_channel_data')")
    public String uploadImage(@PathVariable("channelId") Long channelId, @RequestParam(value = "image", required = true) MultipartFile image, Model model) throws BusinessException {
        channelBO.saveImage(channelId, image);

        model.addAttribute("channel", channelBO.findById(channelId));
        model.addAttribute("success", false);

        return "pages/dashboard/image_upload/channel_img_upl";
    }

    @GetMapping("{channelId}/remove_image")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'mod_channel_data')")
    public String removeImage(@PathVariable("channelId") Long channelId) {
        channelBO.removeImage(channelId);

        return "redirect:/discover/channel/" + channelId;
    }
}
