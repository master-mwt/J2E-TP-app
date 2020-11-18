package it.univaq.disim.mwt.j2etpapp.presentation.rest;

import it.univaq.disim.mwt.j2etpapp.business.AjaxResponse;
import it.univaq.disim.mwt.j2etpapp.business.BusinessException;
import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.utils.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("posts")
public class PostRestController {

    @Autowired
    private PostBO postBO;

    @Autowired
    private UtilsClass utilsClass;

    @DeleteMapping("{postId}/delete")
    @PreAuthorize("hasPermission(#postId, 'it.univaq.disim.mwt.j2etpapp.domain.PostClass', 'delete_post')")
    public ResponseEntity<Object> delete(@PathVariable("postId") String postId) throws BusinessException {
        postBO.deleteById(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("{postId}/upvote")
    public ResponseEntity<Object> doUpvote(@PathVariable("postId") String postId) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        if(principal != null){
            AjaxResponse response = postBO.upvote(postId, principal);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{postId}/downvote")
    public ResponseEntity<Object> doDownvote(@PathVariable("postId") String postId) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        if(principal != null){
            AjaxResponse response = postBO.downvote(postId, principal);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{postId}/hide")
    public ResponseEntity<Object> doHide(@PathVariable("postId") String postId) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        if(principal != null){
            postBO.hideToggle(postId, principal);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{postId}/report")
    public ResponseEntity<Object> doReport(@PathVariable("postId") String postId) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        if(principal != null){
            postBO.reportToggle(postId, principal);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{postId}/save")
    public ResponseEntity<Object> doSave(@PathVariable("postId") String postId) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        if(principal != null){
            postBO.saveToggle(postId, principal);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
