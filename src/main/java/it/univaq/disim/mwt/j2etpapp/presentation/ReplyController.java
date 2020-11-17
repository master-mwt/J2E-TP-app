package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.BusinessException;
import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.business.ReplyBO;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.ReplyClass;
import it.univaq.disim.mwt.j2etpapp.helpers.TemplateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private TemplateHelper templateHelper;

    @PostMapping("create")
    @PreAuthorize("hasPermission(#channelId, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'create_reply')")
    public String save(@Valid @ModelAttribute("reply") ReplyClass reply, BindingResult bindingResult, @RequestParam("postId") String postId, @RequestParam("channelId") Long channelId, Model model) throws BusinessException {

        if(bindingResult.hasErrors()) {
            PostClass post = postBO.findById(postId);
            model.addAttribute("reply", reply);
            model.addAttribute("post", post);
            model.addAttribute("replyPage", replyBO.findByPostOrderByCreatedAtDescPaginated(post, 0, 10));
            model.addAttribute("templateHelper", templateHelper);
            model.addAttribute("errors", bindingResult.getFieldErrors());
            return "pages/discover/post";
        }

        replyBO.createReplyInPost(reply, postBO.findById(postId));

        return "redirect:/discover/post/" + postId;
    }
}
