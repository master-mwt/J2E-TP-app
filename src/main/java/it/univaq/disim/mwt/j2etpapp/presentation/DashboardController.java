package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.*;
import it.univaq.disim.mwt.j2etpapp.configuration.ApplicationProperties;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.RoleClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserChannelRole;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("home")
public class DashboardController {

    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private PostBO postBO;
    @Autowired
    private ReplyBO replyBO;
    @Autowired
    private UserChannelRoleBO userChannelRoleBO;
    @Autowired
    private RoleBO roleBO;
    @Autowired
    private UserBO userBO;
    @Autowired
    private ImageBO imageBO;

    @Autowired
    private ApplicationProperties properties;

    @GetMapping("")
    public String home(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        model.addAttribute("user", principal);
        model.addAttribute("imageBO", imageBO);
        model.addAttribute("dateFormat", properties.getDateFormat());
        model.addAttribute("channel", new ChannelClass());
        return "pages/dashboard/home";
    }

    @GetMapping("channels/owned")
    public String dashboardChannelOwned(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        List<UserChannelRole> userChannelRoles = userChannelRoleBO.findByUserId(principal.getId());
        RoleClass creator = roleBO.findByName("creator");

        List<ChannelClass> channels = new ArrayList<>();
        Map<Long, UserChannelRole> userRoles = new HashMap<>();
        for (UserChannelRole userChannelRole : userChannelRoles) {
            if(creator.equals(userChannelRole.getRole())) {
                channels.add(userChannelRole.getChannel());
                userRoles.put(userChannelRole.getChannel().getId(), userChannelRole);
            }
        }

        model.addAttribute("channels", channels);
        model.addAttribute("userchannelroles", userChannelRoles);
        model.addAttribute("userroles", userRoles);
        return "pages/dashboard/channel_dashboard/list";
    }

    @GetMapping("channels/joined")
    public String dashboardChannelJoined(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        List<UserChannelRole> userChannelRoles = userChannelRoleBO.findByUserId(principal.getId());
        RoleClass creator = roleBO.findByName("creator");

        List<ChannelClass> channels = new ArrayList<>();
        Map<Long, UserChannelRole> userRoles = new HashMap<>();
        for (UserChannelRole userChannelRole : userChannelRoles) {
            if(!creator.equals(userChannelRole.getRole())) {
                channels.add(userChannelRole.getChannel());
                userRoles.put(userChannelRole.getChannel().getId(), userChannelRole);
            }
        }

        model.addAttribute("channels", channels);
        model.addAttribute("userchannelroles", userChannelRoles);
        model.addAttribute("userroles", userRoles);
        return "pages/dashboard/channel_dashboard/list";
    }

    @GetMapping("posts/owned")
    public String dashboardPostOwned(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        model.addAttribute("posts", postBO.findByUserId(principal.getId()));
        model.addAttribute("channelBO", channelBO);
        model.addAttribute("userBO", userBO);
        model.addAttribute("postBO", postBO);
        model.addAttribute("user", principal);

        return "pages/dashboard/post_dashboard/list";
    }

    @GetMapping("posts/saved")
    public String dashboardPostSaved(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        model.addAttribute("posts", postBO.findByUserSaved(principal.getId()));
        model.addAttribute("channelBO", channelBO);
        model.addAttribute("userBO", userBO);
        model.addAttribute("postBO", postBO);
        model.addAttribute("user", principal);

        return "pages/dashboard/post_dashboard/list";
    }

    @GetMapping("posts/hidden")
    public String dashboardPostHidden(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        model.addAttribute("posts", postBO.findByUserHidden(principal.getId()));
        model.addAttribute("channelBO", channelBO);
        model.addAttribute("userBO", userBO);
        model.addAttribute("postBO", postBO);
        model.addAttribute("user", principal);

        return "pages/dashboard/post_dashboard/list";
    }

    @GetMapping("posts/reported")
    public String dashboardPostReported(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        model.addAttribute("posts", postBO.findByUserReported(principal.getId()));
        model.addAttribute("channelBO", channelBO);
        model.addAttribute("userBO", userBO);
        model.addAttribute("postBO", postBO);
        model.addAttribute("user", principal);

        return "pages/dashboard/post_dashboard/list";
    }

    @GetMapping("posts/upvoted")
    public String dashboardPostUpvoted(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        model.addAttribute("posts", postBO.findByUserUpvoted(principal.getId()));
        model.addAttribute("channelBO", channelBO);
        model.addAttribute("userBO", userBO);
        model.addAttribute("postBO", postBO);
        model.addAttribute("user", principal);

        return "pages/dashboard/post_dashboard/list";
    }

    @GetMapping("posts/downvoted")
    public String dashboardPostDownvoted(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        model.addAttribute("posts", postBO.findByUserDownvoted(principal.getId()));
        model.addAttribute("channelBO", channelBO);
        model.addAttribute("userBO", userBO);
        model.addAttribute("postBO", postBO);
        model.addAttribute("user", principal);

        return "pages/dashboard/post_dashboard/list";
    }

    @GetMapping("replies/owned")
    public String dashboardReplyOwned(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        model.addAttribute("replies", replyBO.findByUserId(principal.getId()));
        model.addAttribute("channelBO", channelBO);
        model.addAttribute("userBO", userBO);
        model.addAttribute("replyBO", replyBO);
        model.addAttribute("user", principal);

        return "pages/dashboard/reply_dashboard/list";
    }

    @GetMapping("replies/upvoted")
    public String dashboardReplyUpvoted(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        model.addAttribute("replies", replyBO.findByUserUpvoted(principal.getId()));
        model.addAttribute("channelBO", channelBO);
        model.addAttribute("userBO", userBO);
        model.addAttribute("replyBO", replyBO);
        model.addAttribute("user", principal);

        return "pages/dashboard/reply_dashboard/list";
    }

    @GetMapping("replies/downvoted")
    public String dashboardReplyDownvoted(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        model.addAttribute("replies", replyBO.findByUserDownvoted(principal.getId()));
        model.addAttribute("channelBO", channelBO);
        model.addAttribute("userBO", userBO);
        model.addAttribute("replyBO", replyBO);
        model.addAttribute("user", principal);

        return "pages/dashboard/reply_dashboard/list";
    }
}
