package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
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
@RequestMapping("post")
public class PostController {

    @Autowired
    private PostBO postBO;

    // TODO: is it ok to keep here this functions
    @GetMapping("create")
    public ModelAndView create() {
        // TODO: create post
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("post", new PostClass());
        modelAndView.setViewName("post/form");
        return modelAndView;
    }

    @PostMapping("create")
    public ModelAndView save(@Valid @ModelAttribute("post") PostClass post, Errors errors) {
        // TODO: save post (is ok ?)
        ModelAndView modelAndView = new ModelAndView();

        if(errors.hasErrors()) {
            modelAndView.addObject("errors", errors.getAllErrors());
            modelAndView.setViewName("post/form");
        }
        postBO.save(post);

        modelAndView.setViewName("redirect:/post/view");
        return modelAndView;
    }

    @DeleteMapping("{postId}/delete")
    @PreAuthorize("hasPermission(#postId, 'it.univaq.disim.mwt.j2etpapp.domain.PostClass', 'delete_post')")
    public ModelAndView delete(@PathVariable("postId") String postId) {
        postBO.deleteById(postId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @PostMapping("{postId}/upvote")
    public ResponseEntity doUpvote(@PathVariable("postId") String postId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            PostClass post = postBO.findById(postId);
            if(post.getUsersUpvoted() == null){
                post.setUsersUpvoted(new HashSet<>());
            }
            post.getUsersUpvoted().add(principal.getId());
            postBO.save(post);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{postId}/downvote")
    public ResponseEntity doDownvote(@PathVariable("postId") String postId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            PostClass post = postBO.findById(postId);
            if(post.getUsersDownvoted() == null){
                post.setUsersDownvoted(new HashSet<>());
            }
            post.getUsersDownvoted().add(principal.getId());
            postBO.save(post);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{postId}/hide")
    public ResponseEntity doHide(@PathVariable("postId") String postId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            PostClass post = postBO.findById(postId);
            if(post.getUsersHidden() == null){
                post.setUsersHidden(new HashSet<>());
            }
            post.getUsersHidden().add(principal.getId());
            postBO.save(post);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{postId}/report")
    public ResponseEntity doReport(@PathVariable("postId") String postId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            PostClass post = postBO.findById(postId);
            if(post.getUsersReported() == null){
                post.setUsersReported(new HashSet<>());
            }
            post.getUsersReported().add(principal.getId());
            postBO.save(post);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{postId}/save")
    public ResponseEntity doSave(@PathVariable("postId") String postId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            PostClass post = postBO.findById(postId);
            if(post.getUsersSaved() == null){
                post.setUsersSaved(new HashSet<>());
            }
            post.getUsersSaved().add(principal.getId());
            postBO.save(post);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }
}
