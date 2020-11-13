package it.univaq.disim.mwt.j2etpapp.presentation.rest;

import it.univaq.disim.mwt.j2etpapp.business.BusinessException;
import it.univaq.disim.mwt.j2etpapp.business.NotificationBO;
import it.univaq.disim.mwt.j2etpapp.domain.NotificationClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import it.univaq.disim.mwt.j2etpapp.utils.JSONDealer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("notifications")
public class NotificationRestController {

    @Autowired
    private NotificationBO notificationBO;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllNotifications() throws BusinessException {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        List<NotificationClass> notifications = notificationBO.findByUserTargetId(principal.getId());

        return new ResponseEntity(JSONDealer.ObjectToJSON(notifications), HttpStatus.OK);
    }

    @PostMapping("delete_all")
    public ResponseEntity deleteAllNotifications() {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        notificationBO.deleteAllByUserTargetId(principal.getId());

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("{notificationId}/delete")
    public ResponseEntity deleteNotification(@PathVariable("notificationId") String notificationId) {
        notificationBO.deleteById(notificationId);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("read_all")
    public ResponseEntity readAllNotifications() {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        notificationBO.readAllByUserTargetId(principal.getId());

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("{notificationId}/read")
    public ResponseEntity readNotification(@PathVariable("notificationId") String notificationId) {
        notificationBO.read(notificationId);

        return new ResponseEntity(HttpStatus.OK);
    }
}
