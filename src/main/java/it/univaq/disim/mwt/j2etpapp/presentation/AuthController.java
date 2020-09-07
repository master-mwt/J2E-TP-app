package it.univaq.disim.mwt.j2etpapp.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLogin(){
        return "pages/auth/login";
    }

    // TODO: maybe login controller function ?

    // TODO: maybe login forgot your password function ?

    @GetMapping("/register")
    public String showRegistration(){
        return "pages/auth/register";
    }

    // TODO: register controller function
}
