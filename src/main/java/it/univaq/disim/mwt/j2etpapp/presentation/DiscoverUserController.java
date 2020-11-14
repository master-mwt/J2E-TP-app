package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.Page;
import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.business.UserBO;
import it.univaq.disim.mwt.j2etpapp.configuration.ApplicationProperties;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.helpers.TemplateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("discover/user")
public class DiscoverUserController {

    @Autowired
    private UserBO userBO;
    @Autowired
    private PostBO postBO;

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private TemplateHelper templateHelper;

    @GetMapping("{id}")
    public String discoverUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userBO.findById(id));
        model.addAttribute("dateFormat", properties.getDateFormat());
        return "pages/discover/user";
    }

    @GetMapping("{id}/posts")
    public String discoverUserPosts(@PathVariable("id") Long id, Model model) {
        UserClass user = userBO.findById(id);
        Page<PostClass> postPage = postBO.findByUserIdOrderByCreatedAtDescPaginated(user.getId(), 0, 10);
        model.addAttribute("user", user);
        model.addAttribute("postPage", postPage);
        model.addAttribute("templateHelper", templateHelper);
        return "pages/discover/user_posts";
    }

    @GetMapping("{id}/posts/page/{pageId}")
    public String discoverUserPostsPage(@PathVariable("id") Long id, @PathVariable("pageId") int page, Model model) {
        UserClass user = userBO.findById(id);
        Page<PostClass> postPage = postBO.findByUserIdOrderByCreatedAtDescPaginated(user.getId(), page, 10);
        model.addAttribute("user", user);
        model.addAttribute("postPage", postPage);
        model.addAttribute("templateHelper", templateHelper);
        return "pages/discover/user_posts";
    }
}
