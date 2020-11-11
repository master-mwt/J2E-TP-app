package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.*;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserChannelRole;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("discover/channel")
public class DiscoverChannelController {

    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private UserBO userBO;
    @Autowired
    private PostBO postBO;
    @Autowired
    private UserChannelRoleBO userChannelRoleBO;
    @Autowired
    private TagBO tagBO;

    @GetMapping("{id}")
    public String discoverChannel(@PathVariable("id") Long id, Model model) {
        ChannelClass channel = channelBO.findById(id);
        Page<PostClass> postPage = postBO.findByChannelIdOrderByCreatedAtDescPaginated(id, 0, 10);
        model.addAttribute("post", new PostClass());
        model.addAttribute("channel", channel);
        model.addAttribute("postPage", postPage);
        model.addAttribute("userBO", userBO);
        model.addAttribute("postBO", postBO);
        model.addAttribute("userChannelRoleBO", userChannelRoleBO);
        model.addAttribute("principal", (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null);
        UserChannelRole subscription = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? userChannelRoleBO.findByChannelIdAndUserId(channel.getId(), ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId()) : null;
        model.addAttribute("subscription", subscription);
        model.addAttribute("tags", tagBO.findAll());
        return "pages/discover/channel";
    }

    @GetMapping("{id}/posts/page/{pageId}")
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
        model.addAttribute("tags", tagBO.findAll());
        return "pages/discover/channel";
    }

    @GetMapping("{id}/posts/reported")
    @PreAuthorize("hasPermission(#id, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'global_unreport_post_in_channel')")
    public String discoverChannelPostsReported(@PathVariable("id") Long id, Model model) {
        ChannelClass channel = channelBO.findById(id);
        Page<PostClass> postPage = postBO.findByChannelIdReportedOrderByCreatedAtDescPaginated(id, 0, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("postPage", postPage);
        model.addAttribute("userBO", userBO);
        model.addAttribute("postBO", postBO);
        model.addAttribute("channelBO", channelBO);
        return "pages/discover/reported_posts";
    }

    @GetMapping("{id}/posts/reported/page/{pageId}")
    @PreAuthorize("hasPermission(#id, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'global_unreport_post_in_channel')")
    public String discoverChannelPostsReportedPage(@PathVariable("id") Long id, @PathVariable("pageId") int pageId, Model model) {
        ChannelClass channel = channelBO.findById(id);
        Page<PostClass> postPage = postBO.findByChannelIdReportedOrderByCreatedAtDescPaginated(id, pageId, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("postPage", postPage);
        model.addAttribute("userBO", userBO);
        model.addAttribute("postBO", postBO);
        model.addAttribute("channelBO", channelBO);
        return "pages/discover/reported_posts";
    }

    @GetMapping("{id}/members")
    @PreAuthorize("hasPermission(#id, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'view_channel_members')")
    public String discoverChannelMembers(@PathVariable("id") Long id, Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        ChannelClass channel = channelBO.findById(id);
        Page<UserChannelRole> members = userChannelRoleBO.findByChannelIdPaginated(channel.getId(), 0, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("members", members);
        model.addAttribute("userBO", userBO);
        model.addAttribute("principal", principal);
        model.addAttribute("userRole", userChannelRoleBO.findByChannelIdAndUserId(id, principal.getId()).getRole());
        return "pages/discover/members";
    }

    @GetMapping("{id}/members/page/{pageId}")
    @PreAuthorize("hasPermission(#id, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'view_channel_members')")
    public String discoverChannelMembersPage(@PathVariable("id") Long id, @PathVariable("pageId") int pageId, Model model) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        ChannelClass channel = channelBO.findById(id);
        Page<UserChannelRole> members = userChannelRoleBO.findByChannelIdPaginated(channel.getId(), pageId, 10);
        model.addAttribute("channel", channel);
        model.addAttribute("members", members);
        model.addAttribute("userBO", userBO);
        model.addAttribute("principal", principal);
        model.addAttribute("userRole", userChannelRoleBO.findByChannelIdAndUserId(id, principal.getId()).getRole());
        return "pages/discover/members";
    }

    @GetMapping("{id}/members/banned")
    @PreAuthorize("hasPermission(#id, 'it.univaq.disim.mwt.j2etpapp.domain.ChannelClass', 'softban_user_in_channel')")
    public String discoverChannelMembersBanned(@PathVariable("id") Long id, Model model) {
        ChannelClass channel = channelBO.findById(id);
        model.addAttribute("channel", channel);
        return "pages/discover/banned_users";
    }
}
