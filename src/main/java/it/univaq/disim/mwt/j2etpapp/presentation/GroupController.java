package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.GroupBO;
import it.univaq.disim.mwt.j2etpapp.domain.GroupClass;
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
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupBO groupBO;

    @GetMapping("")
    public String index(Model model){
        List<GroupClass> groupClasses = groupBO.findAll();
        model.addAttribute("groups", groupClasses);
        return "/group/index";
    }

    @GetMapping("create")
    public String create(Model model){
        model.addAttribute("group", new GroupClass());
        return "/group/form";
    }

    @PostMapping("")
    public String store(@Valid @ModelAttribute("group") GroupClass groupClass, Errors errors){
        if(errors.hasErrors()){
            return "/group/form";
        }

        groupBO.save(groupClass);
        return "redirect:/group";
    }

}
