package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.UserBO;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserBO userBO;

    @PostMapping("update")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'mod_user_data')")
    public ModelAndView performUpdate(@Valid @ModelAttribute("user") UserClass user) {
        // TODO: update user is ok ? Errors ?
        ModelAndView modelAndView = new ModelAndView();

        userBO.save(user);

        //modelAndView.addObject(user);
        modelAndView.setViewName("redirect:/home");

        return modelAndView;
    }

    @PostMapping("{userId}/hardban")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'hardban_user_from_platform')")
    public ModelAndView hardBanToggle(@PathVariable("userId") Long userId) {
        userBO.hardBanToggle(userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/home");

        return modelAndView;
    }

    @PostMapping("{userId}/change_password")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'mod_user_data')")
    public ModelAndView changePassword (@RequestParam("old-password") String oldPassword, @RequestParam("new-password") String newPassword, @PathVariable("userId") Long userId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        if(userBO.checkOldPassword(principal, oldPassword)) {
            // old password correct
            userBO.changePassword(principal, newPassword);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/home");

        return modelAndView;
    }

    @PostMapping("{userId}/remove_image")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'mod_user_data')")
    public ModelAndView removeImage(@PathVariable("userId") Long userId) {
        userBO.removeImage(userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/home");

        return modelAndView;
    }
}
