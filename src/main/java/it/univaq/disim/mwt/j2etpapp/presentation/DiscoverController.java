package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.*;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserChannelRole;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
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
    @Autowired
    private ReplyBO replyBO;

    @GetMapping("/discover/channel/{id}")
    public String discoverChannel(@PathVariable("id") Long id, Model model) {
        ChannelClass channel = channelBO.findById(id);
        Page<PostClass> postPage = postBO.findByChannelIdOrderByCreatedAtDescPaginated(id, 0, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("postPage", postPage);
        model.addAttribute("userBO", userBO);
        model.addAttribute("postBO", postBO);
        model.addAttribute("userChannelRoleBO", userChannelRoleBO);
        model.addAttribute("principal", (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null);
        UserChannelRole subscription = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? userChannelRoleBO.findByChannelIdAndUserId(channel.getId(), ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId()) : null;
        model.addAttribute("subscription", subscription);
        return "pages/discover/channel";
    }

    @GetMapping("/discover/channel/{id}/posts/page/{pageId}")
    public String discoverChannelPostsPage(@PathVariable("id") Long id, @PathVariable("pageId") int pageId, Model model) {
        ChannelClass channel = channelBO.findById(id);
        Page<PostClass> postPage = postBO.findByChannelIdOrderByCreatedAtDescPaginated(id, pageId, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("postPage", postPage);
        model.addAttribute("userBO", userBO);
        model.addAttribute("postBO", postBO);
        model.addAttribute("userChannelRoleBO", userChannelRoleBO);
        model.addAttribute("principal", (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null);
        UserChannelRole subscription = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? userChannelRoleBO.findByChannelIdAndUserId(channel.getId(), ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId()) : null;
        model.addAttribute("subscription", subscription);
        return "pages/discover/channel";
    }

    @GetMapping("/discover/channel/{id}/posts/reported")
    public String discoverChannelPostsReported(@PathVariable("id") Long id, Model model) {
        ChannelClass channel = channelBO.findById(id);
        Page<PostClass> postPage = postBO.findByChannelIdReportedOrderByCreatedAtDescPaginated(id, 0, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("postPage", postPage);
        model.addAttribute("userBO", userBO);
        model.addAttribute("channelBO", channelBO);
        return "pages/discover/reported_posts";
    }

    @GetMapping("/discover/channel/{id}/posts/reported/page/{pageId}")
    public String discoverChannelPostsReportedPage(@PathVariable("id") Long id, @PathVariable("pageId") int pageId, Model model) {
        ChannelClass channel = channelBO.findById(id);
        Page<PostClass> postPage = postBO.findByChannelIdReportedOrderByCreatedAtDescPaginated(id, pageId, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("postPage", postPage);
        model.addAttribute("userBO", userBO);
        model.addAttribute("channelBO", channelBO);
        return "pages/discover/reported_posts";
    }

    @GetMapping("/discover/channel/{id}/members")
    public String discoverChannelMembers(@PathVariable("id") Long id, Model model) {
        ChannelClass channel = channelBO.findById(id);
        Page<UserChannelRole> members = userChannelRoleBO.findByChannelIdPaginated(channel.getId(), 0, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("members", members);
        model.addAttribute("userBO", userBO);
        return "pages/discover/members";
    }

    @GetMapping("/discover/channel/{id}/members/page/{pageId}")
    public String discoverChannelMembersPage(@PathVariable("id") Long id, @PathVariable("pageId") int pageId, Model model) {
        ChannelClass channel = channelBO.findById(id);
        Page<UserChannelRole> members = userChannelRoleBO.findByChannelIdPaginated(channel.getId(), pageId, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("members", members);
        model.addAttribute("userBO", userBO);
        return "pages/discover/members";
    }

    @GetMapping("/discover/channel/{id}/members/banned")
    public String discoverChannelMembersBanned(@PathVariable("id") Long id, Model model) {
        ChannelClass channel = channelBO.findById(id);
        model.addAttribute("channel", channel);
        return "pages/discover/banned_users";
    }

    @GetMapping("/discover/user/{id}")
    public String discoverUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userBO.findById(id));
        return "pages/discover/user";
    }

    @GetMapping("/discover/user/{id}/posts")
    public String discoverUserPosts(@PathVariable("id") Long id, Model model) {
        UserClass user = userBO.findById(id);
        Page<PostClass> postPage = postBO.findByUserIdOrderByCreatedAtDescPaginated(user.getId(), 0, 10);
        model.addAttribute("user", user);
        model.addAttribute("postPage", postPage);
        model.addAttribute("postBO", postBO);
        model.addAttribute("userBO", userBO);
        model.addAttribute("channelBO", channelBO);
        return "pages/discover/user_posts";
    }

    @GetMapping("/discover/user/{id}/posts/page/{pageId}")
    public String discoverUserPostsPage(@PathVariable("id") Long id, @PathVariable("pageId") int page, Model model) {
        UserClass user = userBO.findById(id);
        Page<PostClass> postPage = postBO.findByUserIdOrderByCreatedAtDescPaginated(user.getId(), page, 10);
        model.addAttribute("user", user);
        model.addAttribute("postPage", postPage);
        model.addAttribute("postBO", postBO);
        model.addAttribute("userBO", userBO);
        model.addAttribute("channelBO", channelBO);
        return "pages/discover/user_posts";
    }

    @GetMapping("/discover/post/{id}")
    public String discoverPost(@PathVariable("id") String id, Model model) {
        PostClass post = postBO.findById(id);
        model.addAttribute("post", post);
        model.addAttribute("replyPage", replyBO.findByPostOrderByCreatedAtDescPaginated(post, 0, 10));
        model.addAttribute("userBO", userBO);
        model.addAttribute("channelBO", channelBO);
        return "pages/discover/post";
    }

    @GetMapping("/discover/post/{id}/replies/page/{pageId}")
    public String discoverPostPage(@PathVariable("id") String id, @PathVariable("pageId") int page, Model model) {
        PostClass post = postBO.findById(id);
        model.addAttribute("post", post);
        model.addAttribute("replyPage", replyBO.findByPostOrderByCreatedAtDescPaginated(post, page, 10));
        model.addAttribute("userBO", userBO);
        model.addAttribute("channelBO", channelBO);
        return "pages/discover/post";
    }
}
