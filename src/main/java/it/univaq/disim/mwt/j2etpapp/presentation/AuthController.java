package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLogin(){
        return "pages/auth/login";
    }

    // TODO: maybe login controller function ?

    // TODO: maybe login forgot your password function ?

    @GetMapping("/register")
    public String showRegistration(Model model){
        model.addAttribute("user", new UserClass());
        return "pages/auth/register";
    }

    // TODO: incomplete function
    @PostMapping("/register")
    public String performRegistration(@Valid @ModelAttribute("user") UserClass user, Errors errors){
        if(errors.hasErrors()){
            // errors
            System.out.println(errors.getAllErrors());
            // TODO: resolve [Field error in object 'user' on field 'birthDate': rejected value [2020-09-17]; codes [typeMismatch.user.birthDate,typeMismatch.birthDate,typeMismatch.java.time.LocalDate,typeMismatch]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [user.birthDate,birthDate]; arguments []; default message [birthDate]]; default message [Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'birthDate'; nested exception is org.springframework.core.convert.ConversionFailedException: Failed to convert from type [java.lang.String] to type [@javax.persistence.Column java.time.LocalDate] for value '2020-09-17'; nested exception is java.lang.IllegalArgumentException: Parse attempt failed for value [2020-09-17]]]
            System.out.println("errors");
            return "pages/auth/register";
        }

        if(!user.getPassword().equals(user.getMatchingPassword())){
            // error password
            System.out.println("error password");
            return "pages/auth/register";
        }

        System.out.println("success");
        System.out.println(user.getUsername() + " " + user.getEmail());

        return "pages/auth/register";
    }
}
