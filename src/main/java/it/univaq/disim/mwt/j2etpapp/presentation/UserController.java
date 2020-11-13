package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.BusinessException;
import it.univaq.disim.mwt.j2etpapp.business.ImageBO;
import it.univaq.disim.mwt.j2etpapp.business.UserBO;
import it.univaq.disim.mwt.j2etpapp.configuration.ApplicationProperties;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.utils.JSONDealer;
import it.univaq.disim.mwt.j2etpapp.utils.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserBO userBO;
    @Autowired
    private ImageBO imageBO;

    @Autowired
    private ApplicationProperties properties;

    @PostMapping("{userId}/update")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'mod_user_data')")
    public String performUpdate(@Valid @ModelAttribute("user") UserClass newData, @PathVariable("userId") Long userId, Model model, Errors errors) {
        UserClass principal = UtilsClass.getPrincipal();

        if(errors.hasErrors()) {
            // TODO: trovare un modo per far vedere errori: è ok inserire questi dati
            model.addAttribute("user", principal);
            model.addAttribute("imageBO", imageBO);
            model.addAttribute("dateFormat", properties.getDateFormat());
            model.addAttribute("channel", new ChannelClass());
            return "pages/dashboard/home";
        }

        userBO.updateUserProfile(principal, newData);

        return "redirect:/home";
    }

    @GetMapping("{userId}/hardban")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'hardban_user_from_platform')")
    public String hardBanToggle(@PathVariable("userId") Long userId) {
        userBO.hardBanToggle(userId);

        return "redirect:/discover/user/" + userId;
    }

    @GetMapping("{userId}/upgrade_to_administrator")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'upgrade_user_to_administrator')")
    public String upgradeToAdministrator(@PathVariable("userId") Long userId) {
        userBO.upgradeToAdministrator(userId);

        return "redirect:/discover/user/" + userId;
    }

    @GetMapping("{userId}/downgrade_to_logged")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'downgrade_user_to_logged')")
    public String downgradeToLogged(@PathVariable("userId") Long userId) {
        userBO.downgradeToLogged(userId);

        return "redirect:/discover/user/" + userId;
    }

    @PostMapping("{userId}/change_password")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'mod_user_data')")
    public String changePassword (@RequestParam("old-password") String oldPassword, @RequestParam("new-password") String newPassword, @PathVariable("userId") Long userId) {
        UserClass principal = UtilsClass.getPrincipal();

        // TODO: java error to html (errors) ?
        if(userBO.checkOldPassword(principal, oldPassword)) {
            // old password correct
            userBO.changePassword(principal, newPassword);
        }

        return "redirect:/home";
    }

    @GetMapping("{userId}/change_image")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'mod_user_data')")
    public String changeImage(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("user", userBO.findById(userId));

        return "pages/dashboard/image_upload/profile_img_upl";
    }

    @PostMapping("{userId}/change_image")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'mod_user_data')")
    public String uploadImage(@PathVariable("userId") Long userId, @RequestParam(value = "image", required = true) MultipartFile image, RedirectAttributes redirectAttributes) throws BusinessException {
        userBO.saveImage(userId, image);

        redirectAttributes.addAttribute("success", true);
        return "redirect:/user/" + userId + "/change_image";
    }

    @GetMapping("{userId}/remove_image")
    @PreAuthorize("hasPermission(#userId, 'it.univaq.disim.mwt.j2etpapp.domain.UserClass', 'mod_user_data')")
    public String removeImage(@PathVariable("userId") Long userId) {
        userBO.removeImage(userId);

        return "redirect:/home";
    }

    @GetMapping(value = "{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getUser(@PathVariable("userId") Long userId) throws BusinessException {
        UserClass user = userBO.findById(userId);
        Map<String, String> result = new HashMap<>();
        result.put("username", user.getUsername());
        result.put("userImage", user.getImage() == null ? "images/no_profile_img.jpg" : user.getImage().getLocation());

        return JSONDealer.ObjectToJSON(result);
    }
}
