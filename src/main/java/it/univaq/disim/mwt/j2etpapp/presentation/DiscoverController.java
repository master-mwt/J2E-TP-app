package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.*;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserChannelRole;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    private UserChannelRoleBO userChannelRoleBO;

    // TODO: discoverChannel and discoverChannelPostPage passing variables through template
    @GetMapping("/discover/channel/{id}")
    public String discoverChannel(@PathVariable("id") Long id, Model model) {
        ChannelClass channel = channelBO.findById(id);
        Page<PostClass> postPage = postBO.findByChannelIdOrderByCreatedAtDescPaginated(id, 0, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("postPage", postPage);
        model.addAttribute("userBO", userBO);
        model.addAttribute("userChannelRoleBO", userChannelRoleBO);
        model.addAttribute("principal", (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null);
        UserChannelRole subscription = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? userChannelRoleBO.findByChannelIdAndUserId(channel.getId(), ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId()) : null;
        model.addAttribute("subscription", subscription);
        return "pages/discover/channel";
    }

    @GetMapping("/discover/channel/{id}/posts/page/{pageId}")
    public String discoverChannelPostPage(@PathVariable("id") Long id, @PathVariable("pageId") int pageId, Model model) {
        ChannelClass channel = channelBO.findById(id);
        Page<PostClass> postPage = postBO.findByChannelIdOrderByCreatedAtDescPaginated(id, pageId, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("postPage", postPage);
        model.addAttribute("userBO", userBO);
        model.addAttribute("userChannelRoleBO", userChannelRoleBO);
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
