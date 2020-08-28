package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.ChannelBO;
import it.univaq.disim.mwt.j2etpapp.business.Page;
import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.business.UserBO;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("register")
    public String register(Model model) {
        return "pages/auth/register";
    }

    // mylogin in route to not override spring security login page
    @GetMapping("mylogin")
    public String mylogin(Model model) {
        return "pages/auth/login";
    }

    // only for testing
    @GetMapping("empty")
    public String empty(Model model) {
        return "pages/search_res/empty_res";
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
