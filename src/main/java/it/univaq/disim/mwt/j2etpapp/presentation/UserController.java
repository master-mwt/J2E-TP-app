package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.UserBO;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserBO userBO;

    @PostMapping("{userId}/update")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'mod_user_data')")
    public String performUpdate(@Valid @ModelAttribute("user") UserClass newData, @PathVariable("userId") Long userId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        // TODO: update user is ok ? Errors ?
        userBO.updateUserProfile(principal, newData);

        return "redirect:/home";
    }

    @PostMapping("{userId}/hardban")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'hardban_user_from_platform')")
    public String hardBanToggle(@PathVariable("userId") Long userId) {
        userBO.hardBanToggle(userId);

        return "redirect:/home";
    }

    @PostMapping("{userId}/change_password")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'mod_user_data')")
    public String changePassword (@RequestParam("old-password") String oldPassword, @RequestParam("new-password") String newPassword, @PathVariable("userId") Long userId) {
        UserClass principal = (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;

        if(userBO.checkOldPassword(principal, oldPassword)) {
            // old password correct
            userBO.changePassword(principal, newPassword);
        }

        return "redirect:/home";
    }

    @GetMapping("{userId}/change_image")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'mod_user_data')")
    public String changeImage(@PathVariable("userId") Long userId) {
        return "pages/dashboard/image_upload/profile_img_upl";
    }

    @GetMapping("{userId}/remove_image")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'mod_user_data')")
    public String removeImage(@PathVariable("userId") Long userId) {
        userBO.removeImage(userId);

        return "redirect:/home";
    }
}
