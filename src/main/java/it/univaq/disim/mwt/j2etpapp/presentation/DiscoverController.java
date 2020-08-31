package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.ChannelBO;
import it.univaq.disim.mwt.j2etpapp.business.Page;
import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.business.UserBO;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DiscoverController {

    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private UserBO userBO;
    @Autowired
    private PostBO postBO;

    @GetMapping("/discover/channel/{id}")
    public String discoverChannel(@PathVariable("id") Long id, Model model) {
        ChannelClass channel = channelBO.findById(id);
        Page<PostClass> postPage = postBO.findByChannelIdOrderByCreatedAtDescPaginated(id, 0, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("postPage", postPage);
        model.addAttribute("userBO", userBO);
        return "pages/discover/channel";
    }

    @GetMapping("/discover/channel/{id}/posts/page/{pageId}")
    public String discoverChannelPostPage(@PathVariable("id") Long id, @PathVariable("pageId") int pageId, Model model) {
        ChannelClass channel = channelBO.findById(id);
        Page<PostClass> postPage = postBO.findByChannelIdOrderByCreatedAtDescPaginated(id, pageId, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("postPage", postPage);
        model.addAttribute("userBO", userBO);
        return "pages/discover/channel";
    }

    @GetMapping("/discover/user/{id}")
    public String discoverUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userBO.findById(id));
        return "pages/discover/user";
    }

    @GetMapping("/discover/post/{id}")
    public String discoverPost(@PathVariable("id") String id, Model model) {
        model.addAttribute("post", postBO.findById(id));
        return "pages/discover/post";
    }
}
