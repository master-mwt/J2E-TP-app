package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.*;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.TagClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.server.PathParam;

@Controller
public class HomeController {

    @Autowired
    private PostBO postBO;
    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private UserBO userBO;
    @Autowired
    private TagBO tagBO;

    @GetMapping("")
    public String root(Model model) {
        Page<PostClass> postFirstPage = postBO.findAllOrderByCreatedAtDescPaginated(0, 10);
        model.addAttribute("userBO", userBO);
        model.addAttribute("channelBO", channelBO);
        model.addAttribute("postBO", postBO);
        model.addAttribute("page", postFirstPage);
        return "welcome";
    }

    @GetMapping("/posts/page/{page}")
    public String postPaginated(@PathVariable("page") int page, Model model) {
        Page<PostClass> postPage = postBO.findAllOrderByCreatedAtDescPaginated(page, 10);
        model.addAttribute("userBO", userBO);
        model.addAttribute("channelBO", channelBO);
        model.addAttribute("postBO", postBO);
        model.addAttribute("page", postPage);
        return "welcome";
    }

    @GetMapping(value = "/channels/page/{page}")
    public String channelPaginated(@PathVariable("page") int page, @PathParam("q") String query, Model model) {
        Page<ChannelClass> channelPage = channelBO.findByNameContainsPaginated(query, page, 10);
        model.addAttribute("channelPage", channelPage);
        return "search_res/channels_res";
    }

    @GetMapping(value = "/users/page/{page}")
    public String userPaginated(@PathVariable("page") int page, @PathParam("q") String query, Model model) {
        Page<UserClass> userPage = userBO.findByUsernameContainsPaginated(query, page, 10);
        model.addAttribute("userPage", userPage);
        return "search_res/users_res";
    }

    @GetMapping(value = "/tags/page/{page}")
    public String tagPaginated(@PathVariable("page") int page, @PathParam("q") String query, Model model) {
        Page<TagClass> tagPage = tagBO.findByNameContainsPaginated(query, page, 10);
        model.addAttribute("tagPage", tagPage);
        return "search_res/tags_res";
    }
}
