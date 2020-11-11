package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.business.ReplyBO;
import it.univaq.disim.mwt.j2etpapp.domain.ReplyClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@RequestMapping("replies")
@Controller
public class ReplyController {

    @Autowired
    private ReplyBO replyBO;
    @Autowired
    private PostBO postBO;

    @PostMapping("create")
    public String save(@Valid @ModelAttribute("reply") ReplyClass reply, @RequestParam("postId") String postId, Errors errors) {

        if(errors.hasErrors()) {
            // TODO: trovare un modo per far vedere errori
            return "";
        }

        replyBO.createReplyInPost(reply, postBO.findById(postId));

        return "redirect:/discover/post/" + postId;
    }
}
