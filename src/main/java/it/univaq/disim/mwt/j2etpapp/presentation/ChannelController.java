package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.*;
import it.univaq.disim.mwt.j2etpapp.configuration.ApplicationProperties;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserChannelRole;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.helpers.TemplateHelper;
import it.univaq.disim.mwt.j2etpapp.utils.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("channels")
public class ChannelController {

    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private PostBO postBO;
    @Autowired
    private TagBO tagBO;

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private UtilsClass utilsClass;

    @Autowired
    private TemplateHelper templateHelper;

    @PostMapping("create")
    @PreAuthorize("hasAuthority('create_channel')")
    public String save(@Valid @ModelAttribute("channel") ChannelClass channel, BindingResult bindingResult, Model model) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        if(bindingResult.hasErrors()){
            model.addAttribute("user", principal);
            model.addAttribute("templateHelper", templateHelper);
            model.addAttribute("dateFormat", properties.getDateFormat());
            model.addAttribute("channel", channel);
            model.addAttribute("errors", bindingResult.getFieldErrors());
            return "pages/dashboard/home";
        }

        channelBO.createChannel(channel, principal);

        return "redirect:/discover/channel/" + channel.getId();
    }

    @PostMapping("{channelId}")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'delete_channel')")
    public String delete(@PathVariable("channelId") Long channelId) throws BusinessException {
        channelBO.deleteById(channelId);

        return "redirect:/";
    }

    @PostMapping("{channelId}/update")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'mod_channel_data')")
    public String update(@Valid @ModelAttribute("channel") ChannelClass channel, BindingResult bindingResult, @PathVariable("channelId") Long channelId, Model model) throws BusinessException {

        if(bindingResult.hasErrors()){
            ChannelClass channelData = channelBO.findById(channelId);
            Page<PostClass> postPage = postBO.findByChannelIdOrderByCreatedAtDescPaginated(channelId, 0, 10);
            model.addAttribute("post", new PostClass());
            model.addAttribute("channel", channelData);
            model.addAttribute("postPage", postPage);
            model.addAttribute("templateHelper", templateHelper);
            UserClass principal = utilsClass.getPrincipal();
            model.addAttribute("principal", principal);
            UserChannelRole subscription = utilsClass.getSubscription(channel, principal);
            model.addAttribute("subscription", subscription);
            model.addAttribute("tags", tagBO.findAll());
            model.addAttribute("errors", bindingResult.getFieldErrors());
            return "pages/discover/channel";
        }

        channelBO.updateChannel(channelId, channel);

        return "redirect:/discover/channel/" + channelId;
    }

    @GetMapping("{channelId}/join")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'join_channel')")
    public String doJoin(@Valid @PathVariable("channelId") Long channelId) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        channelBO.joinChannel(channelId, principal);

        return "redirect:/discover/channel/" + channelId;
    }

    @GetMapping("{channelId}/leave")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'leave_channel')")
    public String doLeave(@PathVariable("channelId") Long channelId) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

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
    public String doReportUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        channelBO.reportUser(channelId, userId, principal);

        return "redirect:/discover/channel/" + channelId + "/members";
    }

    @GetMapping("{channelId}/members/{userId}/unreport")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'report_user_in_channel')")
    public String doUnReportUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        channelBO.unReportUser(channelId, userId, principal);

        return "redirect:/discover/channel/" + channelId + "/members";
    }

    @GetMapping("{channelId}/members/{userId}/softban")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'softban_user_in_channel')")
    public String doSoftBanUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        channelBO.softBan(channelId, userId, principal);

        return "redirect:/discover/channel/" + channelId + "/members";
    }

    @GetMapping("{channelId}/members/{userId}/unsoftban")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'softban_user_in_channel')")
    public String doUnSoftBanUserInChannel(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        channelBO.unSoftBan(channelId, userId, principal);

        return "redirect:/discover/channel/" + channelId + "/members/banned";
    }

    @GetMapping("{channelId}/members/{userId}/upgrade_member")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'upgrade_member_to_moderator_in_channel')")
    public String upgradeMemberToModerator(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) throws BusinessException {
        channelBO.upgradeMemberToModerator(channelId, userId);

        return "redirect:/discover/channel/" + channelId + "/members";
    }

    @GetMapping("{channelId}/members/{userId}/upgrade_moderator")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'upgrade_moderator_to_admin_in_channel')")
    public String upgradeModeratorToAdmin(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) throws BusinessException {
        channelBO.upgradeModeratorToAdmin(channelId, userId);

        return "redirect:/discover/channel/" + channelId + "/members";
    }

    @GetMapping("{channelId}/members/{userId}/downgrade_moderator")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'downgrade_moderator_to_member_in_channel')")
    public String downgradeModeratorToMember(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) throws BusinessException {
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
    public String downgradeAdminToModerator(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) throws BusinessException {
        channelBO.downgradeAdminToModerator(channelId, userId);

        return "redirect:/discover/channel/" + channelId + "/members";
    }

    @GetMapping("{channelId}/members/{userId}/downgrade_creator")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'downgrade_creator_to_admin_in_channel')")
    public String downgradeCreatorToAdmin(@PathVariable("channelId") Long channelId, @PathVariable("userId") Long userId) throws BusinessException {
        channelBO.downgradeCreatorToAdmin(channelId, userId);

        return "redirect:/discover/channel/" + channelId + "/members";
    }

    @GetMapping("{channelId}/change_image")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'mod_channel_data')")
    public String changeImage(@PathVariable("channelId") Long channelId, Model model) throws BusinessException {
        model.addAttribute("channel", channelBO.findById(channelId));

        return "pages/dashboard/image_upload/channel_img_upl";
    }

    @PostMapping("{channelId}/change_image")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'mod_channel_data')")
    public String uploadImage(@PathVariable("channelId") Long channelId, @RequestParam(value = "image", required = true) MultipartFile image, RedirectAttributes redirectAttributes) throws BusinessException {
        channelBO.saveImage(channelId, image);

        redirectAttributes.addAttribute("success", true);
        return "redirect:/channels/" + channelId + "/change_image";
    }

    @GetMapping("{channelId}/remove_image")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'mod_channel_data')")
    public String removeImage(@PathVariable("channelId") Long channelId) throws BusinessException {
        channelBO.removeImage(channelId);

        return "redirect:/discover/channel/" + channelId;
    }
}
