package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.UserBO;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.utils.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class AdministratorController {

    @Autowired
    private UserBO userBO;

    @GetMapping("")
    public String welcome(Model model) {
        UserClass principal = UtilsClass.getPrincipal();

        model.addAttribute("user", userBO.findById(principal.getId()));

        return "pages/admin/welcome";
    }
}
