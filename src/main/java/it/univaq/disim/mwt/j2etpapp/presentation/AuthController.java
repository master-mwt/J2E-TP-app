package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.AuthBO;
import it.univaq.disim.mwt.j2etpapp.business.BusinessException;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private AuthBO authBO;

    @GetMapping("/login")
    public String showLogin(){
        return "pages/auth/login";
    }

    @GetMapping("/register")
    public String showRegistration(Model model){
        model.addAttribute("user", new UserClass());
        return "pages/auth/register";
    }

    @PostMapping("/register")
    public String performRegistration(@Valid @ModelAttribute("user") UserClass user, @RequestParam("matching-password") String matchingPassword, Errors errors, Model model, RedirectAttributes redirectAttributes) throws BusinessException {
        if(errors.hasErrors()){
            return "pages/auth/register";
        }

        // TODO: automatic error ?
        if(user.getPassword() != null && user.getPassword().length() < 6){
            // error password length
            model.addAttribute("passwordError", "Password must be >= 6");
            return "pages/auth/register";
        }

        if(user.getPassword() != null && !user.getPassword().equals(matchingPassword)){
            // error password
            model.addAttribute("passwordError", "Password are not matching");
            return "pages/auth/register";
        }

        // registration
        authBO.registerUser(user);

        redirectAttributes.addAttribute("registered", true);
        return "redirect:/login";
    }
}
