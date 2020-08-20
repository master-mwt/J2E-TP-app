package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private PostBO postBO;

    @GetMapping("")
    public String root(Model model) {
        List<PostClass> posts = postBO.findAllOrderByCreatedAtDesc();
        model.addAttribute("posts", posts);
        return "welcome";
    }
}
