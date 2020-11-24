package it.univaq.disim.mwt.postd.presentation;

import it.univaq.disim.mwt.postd.business.*;
import it.univaq.disim.mwt.postd.configuration.ApplicationProperties;
import it.univaq.disim.mwt.postd.domain.ChannelClass;
import it.univaq.disim.mwt.postd.domain.RoleClass;
import it.univaq.disim.mwt.postd.domain.UserChannelRole;
import it.univaq.disim.mwt.postd.domain.UserClass;
import it.univaq.disim.mwt.postd.helpers.TemplateHelper;
import it.univaq.disim.mwt.postd.utils.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ApplicationProperties properties;

    @Autowired
    private UtilsClass utilsClass;

    @Autowired
    private TemplateHelper templateHelper;

    @GetMapping("")
    public String home(Model model) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        model.addAttribute("user", userBO.findById(principal.getId()));
        model.addAttribute("templateHelper", templateHelper);
        model.addAttribute("dateFormat", properties.getDateFormat());
        model.addAttribute("channel", new ChannelClass());
        return "pages/dashboard/home";
    }

    @GetMapping("channels/owned")
    public String dashboardChannelOwned(Model model) {
        UserClass principal = utilsClass.getPrincipal();

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
        UserClass principal = utilsClass.getPrincipal();

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
        UserClass principal = utilsClass.getPrincipal();

        model.addAttribute("posts", postBO.findByUserId(principal.getId()));
        model.addAttribute("templateHelper", templateHelper);
        model.addAttribute("user", principal);

        return "pages/dashboard/post_dashboard/list";
    }

    @GetMapping("posts/saved")
    public String dashboardPostSaved(Model model) {
        UserClass principal = utilsClass.getPrincipal();

        model.addAttribute("posts", postBO.findByUserSaved(principal.getId()));
        model.addAttribute("templateHelper", templateHelper);
        model.addAttribute("user", principal);

        return "pages/dashboard/post_dashboard/list";
    }

    @GetMapping("posts/hidden")
    public String dashboardPostHidden(Model model) {
        UserClass principal = utilsClass.getPrincipal();

        model.addAttribute("posts", postBO.findByUserHidden(principal.getId()));
        model.addAttribute("templateHelper", templateHelper);
        model.addAttribute("user", principal);

        return "pages/dashboard/post_dashboard/list";
    }

    @GetMapping("posts/reported")
    public String dashboardPostReported(Model model) {
        UserClass principal = utilsClass.getPrincipal();

        model.addAttribute("posts", postBO.findByUserReported(principal.getId()));
        model.addAttribute("templateHelper", templateHelper);
        model.addAttribute("user", principal);

        return "pages/dashboard/post_dashboard/list";
    }

    @GetMapping("posts/upvoted")
    public String dashboardPostUpvoted(Model model) {
        UserClass principal = utilsClass.getPrincipal();

        model.addAttribute("posts", postBO.findByUserUpvoted(principal.getId()));
        model.addAttribute("templateHelper", templateHelper);
        model.addAttribute("user", principal);

        return "pages/dashboard/post_dashboard/list";
    }

    @GetMapping("posts/downvoted")
    public String dashboardPostDownvoted(Model model) {
        UserClass principal = utilsClass.getPrincipal();

        model.addAttribute("posts", postBO.findByUserDownvoted(principal.getId()));
        model.addAttribute("templateHelper", templateHelper);
        model.addAttribute("user", principal);

        return "pages/dashboard/post_dashboard/list";
    }

    @GetMapping("replies/owned")
    public String dashboardReplyOwned(Model model) {
        UserClass principal = utilsClass.getPrincipal();

        model.addAttribute("replies", replyBO.findByUserId(principal.getId()));
        model.addAttribute("templateHelper", templateHelper);
        model.addAttribute("user", principal);

        return "pages/dashboard/reply_dashboard/list";
    }

    @GetMapping("replies/upvoted")
    public String dashboardReplyUpvoted(Model model) {
        UserClass principal = utilsClass.getPrincipal();

        model.addAttribute("replies", replyBO.findByUserUpvoted(principal.getId()));
        model.addAttribute("templateHelper", templateHelper);
        model.addAttribute("user", principal);

        return "pages/dashboard/reply_dashboard/list";
    }

    @GetMapping("replies/downvoted")
    public String dashboardReplyDownvoted(Model model) {
        UserClass principal = utilsClass.getPrincipal();

        model.addAttribute("replies", replyBO.findByUserDownvoted(principal.getId()));
        model.addAttribute("templateHelper", templateHelper);
        model.addAttribute("user", principal);

        return "pages/dashboard/reply_dashboard/list";
    }
}
