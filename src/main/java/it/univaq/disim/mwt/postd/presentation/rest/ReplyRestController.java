package it.univaq.disim.mwt.postd.presentation.rest;

import it.univaq.disim.mwt.postd.business.AjaxResponse;
import it.univaq.disim.mwt.postd.business.BusinessException;
import it.univaq.disim.mwt.postd.business.ReplyBO;
import it.univaq.disim.mwt.postd.domain.UserClass;
import it.univaq.disim.mwt.postd.utils.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("replies")
public class ReplyRestController {

    @Autowired
    private ReplyBO replyBO;

    @Autowired
    private UtilsClass utilsClass;

    @DeleteMapping("{replyId}/delete")
    @PreAuthorize("hasPermission(#replyId, 'it.univaq.disim.mwt.postd.domain.ReplyClass', 'delete_reply')")
    public ResponseEntity<Object> delete(@PathVariable("replyId") String replyId) {
        replyBO.deleteById(replyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("{replyId}/upvote")
    public ResponseEntity<Object> doUpvote(@PathVariable("replyId") String replyId) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        if(principal != null){
            AjaxResponse response = replyBO.upvote(replyId, principal);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{replyId}/downvote")
    public ResponseEntity<Object> doDownvote(@PathVariable("replyId") String replyId) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        if(principal != null){
            AjaxResponse response = replyBO.downvote(replyId, principal);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
