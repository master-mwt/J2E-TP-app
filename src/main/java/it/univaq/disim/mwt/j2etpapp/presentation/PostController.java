package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.*;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserChannelRole;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import it.univaq.disim.mwt.j2etpapp.utils.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequestMapping("posts")
public class PostController {

    @Autowired
    private PostBO postBO;
    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private UserBO userBO;
    @Autowired
    private UserChannelRoleBO userChannelRoleBO;
    @Autowired
    private TagBO tagBO;

    @PostMapping("create")
    public String save(@Valid @ModelAttribute("post") PostClass post, BindingResult bindingResult, @RequestParam("tags") String tags, @RequestParam("files") MultipartFile[] images, Model model) throws BusinessException {

        if(bindingResult.hasErrors()) {
            ChannelClass channel = channelBO.findById(post.getChannelId());
            Page<PostClass> postPage = postBO.findByChannelIdOrderByCreatedAtDescPaginated(post.getChannelId(), 0, 10);
            model.addAttribute("post", post);
            model.addAttribute("channel", channel);
            model.addAttribute("postPage", postPage);
            model.addAttribute("userBO", userBO);
            model.addAttribute("postBO", postBO);
            model.addAttribute("userChannelRoleBO", userChannelRoleBO);
            UserClass principal = UtilsClass.getPrincipal();
            model.addAttribute("principal", principal);
            UserChannelRole subscription = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? userChannelRoleBO.findByChannelIdAndUserId(channel.getId(), principal.getId()) : null;
            model.addAttribute("subscription", subscription);
            model.addAttribute("tags", tagBO.findAll());
            model.addAttribute("errors", bindingResult.getFieldErrors());
            return "pages/discover/channel";
        }
        postBO.createPostInChannel(post, tags, images);

        return "redirect:/discover/post/" + post.getId();
    }
}
