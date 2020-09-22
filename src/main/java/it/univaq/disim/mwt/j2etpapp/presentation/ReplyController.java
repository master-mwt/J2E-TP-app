package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.ReplyBO;
import it.univaq.disim.mwt.j2etpapp.domain.ReplyClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashSet;

@RestController
@RequestMapping("reply")
public class ReplyController {

    @Autowired
    private ReplyBO replyBO;

    // TODO: is it ok to keep here this functions
    @GetMapping("create")
    public ModelAndView create() {
        // TODO: create reply
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("reply", new ReplyClass());
        modelAndView.setViewName("reply/form");
        return modelAndView;
    }

    @PostMapping("create")
    public ModelAndView save(@Valid @ModelAttribute("reply") ReplyClass reply, Errors errors) {
        // TODO: save reply (is ok ?)
        ModelAndView modelAndView = new ModelAndView();

        if(errors.hasErrors()) {
            modelAndView.addObject("errors", errors.getAllErrors());
            modelAndView.setViewName("reply/form");
        }
        replyBO.save(reply);

        modelAndView.setViewName("redirect:/reply/view");
        return modelAndView;
    }

    @DeleteMapping("{replyId}/delete")
    @PreAuthorize("hasPermission(#replyId, 'it.univaq.disim.mwt.j2etpapp.domain.ReplyClass', 'delete_reply')")
    public ModelAndView delete(@PathVariable("replyId") String replyId) {
        replyBO.deleteById(replyId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @PostMapping("{replyId}/upvote")
    public ResponseEntity doUpvote(@PathVariable("replyId") String replyId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            ReplyClass reply = replyBO.findById(replyId);
            if(reply.getUsersUpvoted() == null){
                reply.setUsersUpvoted(new HashSet<>());
            }
            reply.getUsersUpvoted().add(principal.getId());
            replyBO.save(reply);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{replyId}/downvote")
    public ResponseEntity doDownvote(@PathVariable("replyId") String replyId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            ReplyClass reply = replyBO.findById(replyId);
            if(reply.getUsersDownvoted() == null){
                reply.setUsersDownvoted(new HashSet<>());
            }
            reply.getUsersDownvoted().add(principal.getId());
            replyBO.save(reply);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }
}
