package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.ChannelBO;
import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.business.ReplyBO;
import it.univaq.disim.mwt.j2etpapp.business.UserBO;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.ReplyClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    @Autowired
    private UserBO userBO;
    @Autowired
    private ChannelBO channelBO;

    @PostMapping("create")
    public String save(@Valid @ModelAttribute("reply") ReplyClass reply, @RequestParam("postId") String postId, Model model, Errors errors) {

        if(errors.hasErrors()) {
            // TODO: trovare un modo per far vedere errori: è ok inserire questi dati
            PostClass post = postBO.findById(postId);
            model.addAttribute("reply", new ReplyClass());
            model.addAttribute("post", post);
            model.addAttribute("replyPage", replyBO.findByPostOrderByCreatedAtDescPaginated(post, 0, 10));
            model.addAttribute("userBO", userBO);
            model.addAttribute("postBO", postBO);
            model.addAttribute("replyBO", replyBO);
            model.addAttribute("channelBO", channelBO);
            return "pages/discover/post";
        }

        replyBO.createReplyInPost(reply, postBO.findById(postId));

        return "redirect:/discover/post/" + postId;
    }
}
