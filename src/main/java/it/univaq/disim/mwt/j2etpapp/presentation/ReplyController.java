package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.ReplyBO;
import it.univaq.disim.mwt.j2etpapp.domain.ReplyClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

@RestController
@RequestMapping("reply")
public class ReplyController {

    @Autowired
    private ReplyBO replyBO;

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
