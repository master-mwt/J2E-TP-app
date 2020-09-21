package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.ChannelBO;
import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.business.ReplyBO;
import it.univaq.disim.mwt.j2etpapp.business.UserChannelRoleBO;
import it.univaq.disim.mwt.j2etpapp.domain.*;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Controller
public class DashboardController {

    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private PostBO postBO;
    @Autowired
    private ReplyBO replyBO;
    @Autowired
    private UserChannelRoleBO userChannelRoleBO;

    @GetMapping("home")
    public String home(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        model.addAttribute("user", principal);
        return "pages/dashboard/home";
    }

    @GetMapping("home/channels")
    public String dashboardChannel(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        List<UserChannelRole> userChannelRoles = userChannelRoleBO.findByUserId(principal.getId());

        List<ChannelClass> channels = new ArrayList<>();
        Map<Long, UserChannelRole> userRoles = new HashMap<>();
        for (UserChannelRole userChannelRole : userChannelRoles) {
            channels.add(userChannelRole.getChannel());
            userRoles.put(userChannelRole.getChannel().getId(), userChannelRole);
        }

        model.addAttribute("channels", channels);
        model.addAttribute("userchannelroles", userChannelRoles);
        model.addAttribute("userroles", userRoles);
        return "pages/dashboard/channel_dashboard/list";
    }

    @GetMapping("home/channel/{id}/image/upload")
    public String dashboardImageUploadChannel(@PathVariable("id") Long channelId, Model model) {
        model.addAttribute("channel", channelBO.findById(channelId));
        return "pages/dashboard/image_upload/channel_img_upl";
    }

    @GetMapping("home/profile/image/upload")
    public String dashboardImageUploadProfile(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        model.addAttribute("user", principal);
        return "pages/dashboard/image_upload/profile_img_upl";
    }

    @GetMapping("home/posts")
    public String dashboardPost(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        // saved, hidden, reported, ...
        Set<PostClass> posts = new HashSet<>();
        posts.addAll(postBO.findByUserSaved(principal.getId()));
        posts.addAll(postBO.findByUserUpvoted(principal.getId()));
        posts.addAll(postBO.findByUserDownvoted(principal.getId()));
        posts.addAll(postBO.findByUserHidden(principal.getId()));
        posts.addAll(postBO.findByUserReported(principal.getId()));

        model.addAttribute("posts", posts);
        model.addAttribute("channelBO", channelBO);
        model.addAttribute("user", principal);

        return "pages/dashboard/post_dashboard/list";
    }

    @GetMapping("home/replies")
    public String dashboardReply(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        // downvoted, upvoted
        Set<ReplyClass> replies = new HashSet<>();
        replies.addAll(replyBO.findByUserUpvoted(principal.getId()));
        replies.addAll(replyBO.findByUserDownvoted(principal.getId()));

        model.addAttribute("replies", replies);
        model.addAttribute("channelBO", channelBO);
        model.addAttribute("user", principal);

        return "pages/dashboard/reply_dashboard/list";
    }
}
