package it.univaq.disim.mwt.postd.presentation;

import it.univaq.disim.mwt.postd.business.AuthBO;
import it.univaq.disim.mwt.postd.business.BusinessException;
import it.univaq.disim.mwt.postd.domain.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String performRegistration(@Valid @ModelAttribute("user") UserClass user, BindingResult bindingResult, @RequestParam("matching-password") String matchingPassword, Model model, RedirectAttributes redirectAttributes) throws BusinessException {
        if(bindingResult.hasErrors()){
            model.addAttribute("user", user);
            return "pages/auth/register";
        }

        if(user.getPassword() != null && !user.getPassword().equals(matchingPassword)){
            // error password match
            model.addAttribute("user", user);
            model.addAttribute("errors", "matchPasswordError");
            return "pages/auth/register";
        }

        // registration
        authBO.registerUser(user);

        redirectAttributes.addAttribute("registered", true);
        return "redirect:/login";
    }
}
