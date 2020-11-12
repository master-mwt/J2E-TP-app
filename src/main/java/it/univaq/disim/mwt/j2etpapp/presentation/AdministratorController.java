package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.*;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class AdministratorController {

    @Autowired
    private UserBO userBO;
    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private UserChannelRoleBO userChannelRoleBO;
    @Autowired
    private RoleBO roleBO;
    @Autowired
    private PostBO postBO;

    @GetMapping("")
    public String welcome() {
        // TODO: welcome screen for administrator
        return "";
    }

    @GetMapping("search/users")
    public String getAllUsers(Model model) {

        Page<UserClass> userPage = userBO.findByUsernameContainsPaginated("", 0, 10);

        model.addAttribute("userPage", userPage);
        return "pages/search_res/users_res";
    }

    @GetMapping("search/channels")
    public String getAllChannels(Model model) {
        Page<ChannelClass> channelPage = channelBO.findByNameContainsPaginated("", 0, 10);

        model.addAttribute("channelPage", channelPage);
        model.addAttribute("userChannelRoleBO", userChannelRoleBO);
        model.addAttribute("roleBO", roleBO);
        return "pages/search_res/channels_res";
    }

    @GetMapping("search/posts")
    public String getAllPosts(Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        Page<PostClass> postPage = postBO.findByTitleContainsPaginated("", 0, 10);

        model.addAttribute("postPage", postPage);
        model.addAttribute("channelBO", channelBO);
        model.addAttribute("userBO", userBO);
        model.addAttribute("postBO", postBO);
        model.addAttribute("principal", principal);
        return "pages/search_res/posts_res";
    }
}
