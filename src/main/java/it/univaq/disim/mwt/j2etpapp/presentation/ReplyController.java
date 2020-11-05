package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.AjaxResponse;
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

@RestController
@RequestMapping("replies")
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
    public ResponseEntity delete(@PathVariable("replyId") String replyId) {
        replyBO.deleteById(replyId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("{replyId}/upvote")
    public ResponseEntity doUpvote(@PathVariable("replyId") String replyId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            AjaxResponse response = replyBO.upvote(replyId, principal);
            return new ResponseEntity<AjaxResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{replyId}/downvote")
    public ResponseEntity doDownvote(@PathVariable("replyId") String replyId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            AjaxResponse response = replyBO.downvote(replyId, principal);
            return new ResponseEntity<AjaxResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }
}
