package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.GroupBO;
import it.univaq.disim.mwt.j2etpapp.domain.Group;
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
        List<Group> groups = groupBO.findAllGroups();
        model.addAttribute("groups", groups);
        return "/group/index";
    }

    @GetMapping("create")
    public String create(Model model){
        model.addAttribute("group", new Group());
        return "/group/form";
    }

    @PostMapping("")
    public String store(@Valid @ModelAttribute("group") Group group, Errors errors){
        if(errors.hasErrors()){
            return "/group/form";
        }

        groupBO.save(group);
        return "redirect:/group";
    }

}
