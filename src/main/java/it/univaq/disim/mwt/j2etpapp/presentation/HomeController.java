package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.ChannelBO;
import it.univaq.disim.mwt.j2etpapp.business.Page;
import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.business.UserBO;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @Autowired
    private PostBO postBO;
    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private UserBO userBO;

    @GetMapping("")
    public String root(Model model) {
        Page<PostClass> postFirstPage = postBO.findAllOrderByCreatedAtDescPaginated(0, 10);
        model.addAttribute("userBO", userBO);
        model.addAttribute("channelBO", channelBO);
        model.addAttribute("postBO", postBO);
        model.addAttribute("page", postFirstPage);
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        model.addAttribute("user", principal);
        return "pages/welcome";
    }

    @GetMapping("about")
    public String about(Model model) {
        return "pages/info/about";
    }

    @GetMapping("help")
    public String help(Model model) {
        return "pages/info/help";
    }

    @GetMapping("contact")
    public String contact(Model model) {
        return "pages/info/contact";
    }

    // only for testing
    @GetMapping("empty")
    public String empty(Model model) {
        return "pages/search_res/empty_res";
    }

    // only for testing
    @GetMapping("channel")
    public String channel(Model model) {
        return "pages/discover/channel";
    }

    @GetMapping("/posts/page/{page}")
    public String postPaginated(@PathVariable("page") int page, Model model) {
        Page<PostClass> postPage = postBO.findAllOrderByCreatedAtDescPaginated(page, 10);
        model.addAttribute("userBO", userBO);
        model.addAttribute("channelBO", channelBO);
        model.addAttribute("postBO", postBO);
        model.addAttribute("page", postPage);
        return "pages/welcome";
    }
}
