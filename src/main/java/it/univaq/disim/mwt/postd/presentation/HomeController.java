package it.univaq.disim.mwt.postd.presentation;

import it.univaq.disim.mwt.postd.business.Page;
import it.univaq.disim.mwt.postd.business.PostBO;
import it.univaq.disim.mwt.postd.domain.PostClass;
import it.univaq.disim.mwt.postd.domain.UserClass;
import it.univaq.disim.mwt.postd.helpers.TemplateHelper;
import it.univaq.disim.mwt.postd.utils.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @Autowired
    private PostBO postBO;

    @Autowired
    private UtilsClass utilsClass;

    @Autowired
    private TemplateHelper templateHelper;

    @GetMapping("login_success")
    public String defaultAfterLogin(HttpServletRequest req) {
        if(req.isUserInRole("administrator")) {
            return "redirect:/admin/welcome";
        }
        return "redirect:/";
    }

    @GetMapping("")
    public String welcome(Model model) {
        Page<PostClass> postFirstPage = postBO.findAllOrderByCreatedAtDescPaginated(0, 10);
        model.addAttribute("templateHelper", templateHelper);
        model.addAttribute("page", postFirstPage);
        UserClass principal = utilsClass.getPrincipal();
        model.addAttribute("user", principal);
        return "pages/welcome";
    }

    @GetMapping("/posts/page/{page}")
    public String welcomePostPaginated(@PathVariable("page") int page, Model model) {
        Page<PostClass> postPage = postBO.findAllOrderByCreatedAtDescPaginated(page, 10);
        model.addAttribute("templateHelper", templateHelper);
        model.addAttribute("page", postPage);
        UserClass principal = utilsClass.getPrincipal();
        model.addAttribute("user", principal);
        return "pages/welcome";
    }

    @GetMapping("about")
    public String about() {
        return "pages/info/about";
    }

    @GetMapping("contact")
    public String contact() {
        return "pages/info/contact";
    }
}
