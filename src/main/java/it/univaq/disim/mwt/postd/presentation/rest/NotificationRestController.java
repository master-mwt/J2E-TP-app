package it.univaq.disim.mwt.postd.presentation.rest;

import it.univaq.disim.mwt.postd.business.NotificationBO;
import it.univaq.disim.mwt.postd.domain.NotificationClass;
import it.univaq.disim.mwt.postd.domain.UserClass;
import it.univaq.disim.mwt.postd.utils.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("notifications")
public class NotificationRestController {

    @Autowired
    private NotificationBO notificationBO;

    @Autowired
    private UtilsClass utilsClass;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllNotifications() {
        UserClass principal = utilsClass.getPrincipal();

        if(principal != null) {
            List<NotificationClass> notifications = notificationBO.findByUserTargetId(principal.getId());
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("delete_all")
    public ResponseEntity<Object> deleteAllNotifications() {
        UserClass principal = utilsClass.getPrincipal();

        if(principal != null) {
            notificationBO.deleteAllByUserTargetId(principal.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("{notificationId}/delete")
    public ResponseEntity<Object> deleteNotification(@PathVariable("notificationId") String notificationId) {
        notificationBO.deleteById(notificationId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
