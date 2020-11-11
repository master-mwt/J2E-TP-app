package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("posts")
public class PostController {

    @Autowired
    private PostBO postBO;

    @PostMapping("create")
    public String save(@Valid @ModelAttribute("post") PostClass post, Errors errors) {

        if(errors.hasErrors()) {
            // TODO: trovare un modo per far vedere errori
            return "";
        }
        postBO.createPostInChannel(post);

        return "redirect:/discover/post/" + post.getId();
    }
}
