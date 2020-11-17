package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.BusinessException;
import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.business.ReplyBO;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.ReplyClass;
import it.univaq.disim.mwt.j2etpapp.helpers.TemplateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("discover/post")
public class DiscoverPostController {

    @Autowired
    private PostBO postBO;
    @Autowired
    private ReplyBO replyBO;

    @Autowired
    private TemplateHelper templateHelper;

    @GetMapping("{id}")
    public String discoverPost(@PathVariable("id") String id, Model model) throws BusinessException {
        PostClass post = postBO.findById(id);
        model.addAttribute("reply", new ReplyClass());
        model.addAttribute("post", post);
        model.addAttribute("replyPage", replyBO.findByPostOrderByCreatedAtDescPaginated(post, 0, 10));
        model.addAttribute("templateHelper", templateHelper);
        return "pages/discover/post";
    }

    @GetMapping("{id}/replies/page/{pageId}")
    public String discoverPostPage(@PathVariable("id") String id, @PathVariable("pageId") int page, Model model) throws BusinessException {
        PostClass post = postBO.findById(id);
        model.addAttribute("post", post);
        model.addAttribute("replyPage", replyBO.findByPostOrderByCreatedAtDescPaginated(post, page, 10));
        model.addAttribute("templateHelper", templateHelper);
        return "pages/discover/post";
    }
}
