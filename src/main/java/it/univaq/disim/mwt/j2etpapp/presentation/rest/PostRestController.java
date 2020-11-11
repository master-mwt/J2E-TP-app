package it.univaq.disim.mwt.j2etpapp.presentation.rest;

import it.univaq.disim.mwt.j2etpapp.business.AjaxResponse;
import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("posts")
public class PostRestController {

    @Autowired
    private PostBO postBO;

    @DeleteMapping("{postId}/delete")
    @PreAuthorize("hasPermission(#postId, 'it.univaq.disim.mwt.j2etpapp.domain.PostClass', 'delete_post')")
    public ResponseEntity delete(@PathVariable("postId") String postId) {
        postBO.deleteById(postId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("{postId}/upvote")
    public ResponseEntity doUpvote(@PathVariable("postId") String postId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            AjaxResponse response = postBO.upvote(postId, principal);
            return new ResponseEntity<AjaxResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{postId}/downvote")
    public ResponseEntity doDownvote(@PathVariable("postId") String postId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            AjaxResponse response = postBO.downvote(postId, principal);
            return new ResponseEntity<AjaxResponse>(response, HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{postId}/hide")
    public ResponseEntity doHide(@PathVariable("postId") String postId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            postBO.hideToggle(postId, principal);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{postId}/report")
    public ResponseEntity doReport(@PathVariable("postId") String postId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            postBO.reportToggle(postId, principal);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{postId}/save")
    public ResponseEntity doSave(@PathVariable("postId") String postId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            postBO.saveToggle(postId, principal);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }
}
