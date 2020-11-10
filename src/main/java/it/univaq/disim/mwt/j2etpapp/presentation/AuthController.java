package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.AuthBO;
import it.univaq.disim.mwt.j2etpapp.business.BusinessException;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AuthController {

    private static PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private AuthBO authBO;

    @GetMapping("/login")
    public String showLogin(){
        return "pages/auth/login";
    }

    // TODO: maybe login forgot your password function ?

    @GetMapping("/register")
    public String showRegistration(Model model){
        model.addAttribute("user", new UserClass());
        return "pages/auth/register";
    }

    @PostMapping("/register")
    public String performRegistration(@Valid @ModelAttribute("user") UserClass user, Errors errors, Model model, RedirectAttributes redirectAttributes) throws BusinessException {
        if(errors.hasErrors()){
            // errors
            System.out.println(errors.getAllErrors());
            return "pages/auth/register";
        }

        // TODO: automatic error ?
        if(user.getPassword() != null && user.getPassword().length() < 6){
            // error password length
            model.addAttribute("passwordError", "Password must be >= 6");
            return "pages/auth/register";
        }

        if(user.getPassword() != null && !user.getPassword().equals(user.getMatchingPassword())){
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
