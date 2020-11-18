package it.univaq.disim.mwt.postd.presentation;

import it.univaq.disim.mwt.postd.business.BusinessException;
import it.univaq.disim.mwt.postd.business.UserBO;
import it.univaq.disim.mwt.postd.domain.UserClass;
import it.univaq.disim.mwt.postd.utils.UtilsClass;
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

    @Autowired
    private UtilsClass utilsClass;

    @GetMapping("welcome")
    public String welcome(Model model) throws BusinessException {
        UserClass principal = utilsClass.getPrincipal();

        model.addAttribute("user", userBO.findById(principal.getId()));

        return "pages/admin/welcome";
    }
}
