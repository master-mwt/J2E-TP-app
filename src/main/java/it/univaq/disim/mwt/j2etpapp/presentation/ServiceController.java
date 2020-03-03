package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.ServiceBO;
import it.univaq.disim.mwt.j2etpapp.domain.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    private ServiceBO serviceBO;

    @GetMapping("")
    public String index(Model model){
        List<Service> services = serviceBO.findAllServices();
        model.addAttribute("services", services);
        return "/service/index";
    }

    @GetMapping("create")
    public String create(Model model){
        model.addAttribute("service", new Service());
        return "/service/form";
    }

    @PostMapping("")
    public String store(@Valid @ModelAttribute("service") Service service, Errors errors){
        if(errors.hasErrors()){
            return "/service/form";
        }

        serviceBO.save(service);
        return "redirect:/service";
    }

}
