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
public class SearchController {

    @Autowired
    private PostBO postBO;
    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private UserBO userBO;
    @Autowired
    private TagBO tagBO;
    @Autowired
    private UserChannelRoleBO userChannelRoleBO;
    @Autowired
    private RoleBO roleBO;

    @GetMapping(value = "/search")
    public String search(@PathParam("target") String target, @PathParam("query") String query, Model model) {
        if(target != null){
            switch (target){
                case "posts":
                    Page<PostClass> postPage = postBO.findByTitleContainsPaginated(query, 0, 10);
                    model.addAttribute("postPage", postPage);
                    return "pages/search_res/posts_res";
                case "channels":
                    Page<ChannelClass> channelPage = channelBO.findByNameContainsPaginated(query, 0, 10);
                    model.addAttribute("channelPage", channelPage);
                    model.addAttribute("userChannelRoleBO", userChannelRoleBO);
                    model.addAttribute("roleBO", roleBO);
                    return "pages/search_res/channels_res";
                case "users":
                    Page<UserClass> userPage = userBO.findByUsernameContainsPaginated(query, 0, 10);
                    model.addAttribute("userPage", userPage);
                    return "pages/search_res/users_res";
                case "tags":
                    Page<TagClass> tagPage = tagBO.findByNameContainsPaginated(query, 0, 10);
                    model.addAttribute("tagPage", tagPage);
                    return "pages/search_res/tags_res";
                default:
                    return "redirect:/";
            }
        } else {
            return "redirect:/";
        }
    }

    @GetMapping(value = "/search/posts/page/{page}")
    public String postPaginated(@PathVariable(name = "page") int page, @PathParam("query") String query, Model model) {
        Page<PostClass> postPage = postBO.findByTitleContainsPaginated(query, page, 10);
        model.addAttribute("postPage", postPage);
        return "pages/search_res/posts_res";
    }

    @GetMapping(value = "/search/channels/page/{page}")
    public String channelPaginated(@PathVariable(name = "page") int page, @PathParam("query") String query, Model model) {
        Page<ChannelClass> channelPage = channelBO.findByNameContainsPaginated(query, page, 10);
        model.addAttribute("channelPage", channelPage);
        model.addAttribute("userChannelRoleBO", userChannelRoleBO);
        model.addAttribute("roleBO", roleBO);
        return "pages/search_res/channels_res";
    }

    @GetMapping(value = "/search/users/page/{page}")
    public String userPaginated(@PathVariable(name = "page") int page, @PathParam("query") String query, Model model) {
        Page<UserClass> userPage = userBO.findByUsernameContainsPaginated(query, page, 10);
        model.addAttribute("userPage", userPage);
        return "pages/search_res/users_res";
    }

    @GetMapping(value = "/search/tags/page/{page}")
    public String tagPaginated(@PathVariable(name = "page") int page, @PathParam("query") String query, Model model) {
        Page<TagClass> tagPage = tagBO.findByNameContainsPaginated(query, page, 10);
        model.addAttribute("tagPage", tagPage);
        return "pages/search_res/tags_res";
    }
}
