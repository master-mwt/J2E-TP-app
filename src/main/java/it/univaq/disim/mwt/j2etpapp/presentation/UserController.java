package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.UserBO;
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
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserBO userBO;

    @GetMapping("update")
    public ModelAndView update() {
        // TODO: update user
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", principal);
        modelAndView.setViewName("user/update");

        return modelAndView;
    }

    @PostMapping("update")
    public ModelAndView performUpdate(@Valid @ModelAttribute("user") UserClass user, Errors errors) {
        // TODO: update user is ok ?
        ModelAndView modelAndView = new ModelAndView();
        if(errors.hasErrors()) {
            modelAndView.addObject(errors.getAllErrors());
            modelAndView.setViewName("user/update");
        }
        userBO.save(user);
        modelAndView.addObject(user);
        modelAndView.setViewName("redirect:/home");

        return modelAndView;
    }

    @PostMapping("{userId}/hardban")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'hardban_user_from_platform')")
    public ResponseEntity hardBanToggle(@PathVariable("userId") Long userId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
        if(principal != null){
            userBO.hardBanToggle(userId);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("Login requested", HttpStatus.UNAUTHORIZED);
    }
}
