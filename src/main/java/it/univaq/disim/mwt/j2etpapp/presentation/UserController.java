package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.business.UserBO;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserBO userBO;

    // index
    @GetMapping("")
    public String index(Model model){
        List<UserClass> userClasses = userBO.findAll();
        model.addAttribute("users", userClasses);
        return "/user/index";
    }

    // save
    @GetMapping("create")
    public String create(Model model){
        model.addAttribute("user", new UserClass());
        return "/user/form";
    }

    @PostMapping("")
    public String store(@Valid @ModelAttribute("user") UserClass userClass, Errors errors){
        if(errors.hasErrors()){
            return "/user/form";
        }
        userBO.save(userClass);
        return "redirect:/user";
    }

    // show
    @GetMapping("{id}")
    public String show(@PathVariable("id") Long id, Model model){
        UserClass userClass = userBO.findById(id);
        model.addAttribute("user", userClass);
        return "/user/show";
    }

    // update
    @GetMapping("{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model){
        UserClass userClass = userBO.findById(id);
        model.addAttribute("user", userClass);
        return "/user/form";
    }

    @PutMapping("{id}")
    public String update(@Valid @ModelAttribute("user") UserClass userClass, Errors errors){
        if(errors.hasErrors()){
            return "/user/form";
        }
        userBO.save(userClass);
        return "redirect:/user";
    }

    // delete
    @DeleteMapping("{id}")
    public String destroy(@PathVariable("id") Long id, Model model){
        userBO.deleteById(id);
        model.addAttribute("users", userBO.findAll());
        return "/user/index";
    }

}
