package it.univaq.disim.mwt.j2etpapp.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("")
    public String root() {
        return "<h1>Welcome</h1>";
    }

    @GetMapping("/home")
    public String home() {
        return "<h1>Welcome logged</h1>";
    }

    @GetMapping("/admin")
    public String admin() {
        return "<h1>Welcome admin</h1>";
    }
}
