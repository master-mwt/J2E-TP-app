package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.ChannelBO;
import it.univaq.disim.mwt.j2etpapp.business.Page;
import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @Autowired
    private PostBO postBO;
    @Autowired
    private ChannelBO channelBO;

    @GetMapping("")
    public String root(Model model) {
        Page<PostClass> postFirstPage = postBO.findAllOrderByCreatedAtDescPaginated(0, 10);
        model.addAttribute("page", postFirstPage);
        return "welcome";
    }

    // TODO: channel test method return text (pagination)
    @GetMapping(value = "/channels/page/{page}", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String channels(@PathVariable("page") int page) {
        Page<ChannelClass> channelPage = channelBO.findByNameContainsPaginated("", page, 10);
        return channelPage.toString();
    }

    // TODO: post test method return page with information inside (pagination)
    @GetMapping("/posts/page/{page}")
    public String postPaginated(@PathVariable("page") int page, Model model) {
        Page<PostClass> postPage = postBO.findAllOrderByCreatedAtDescPaginated(page, 10);
        model.addAttribute("page", postPage);
        return "welcome";
    }
}
