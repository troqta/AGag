package com.accenture.controllers;

import com.accenture.entities.BindingModels.UserEditModel;
import com.accenture.entities.User;
import com.accenture.services.Base.UserService;
import com.accenture.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.UndeclaredThrowableException;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {

        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("view", "/user/profile");
        return "base-layout";
    }

    @GetMapping("/edit")
    public String editProfile(Model model) {

        User user = userService.getCurrentUser();

        model.addAttribute("user", user);
        model.addAttribute("view", "/user/edit");

        return "base-layout";
    }

    @PostMapping("/edit")
    public String handleEdit(UserEditModel editModel, @RequestParam("file") MultipartFile file) {
        userService.editUser(userService.getCurrentUser(), editModel, file);

        return "redirect:/user/profile";
    }

    @GetMapping("/{id}")
    public String viewUser(Model model, @PathVariable("id") int id) {
        User user = userService.findById(id);
        if(!Util.isAnonymous()){
            if (user.getId() == userService.getCurrentUser().getId())
                return"redirect:/user/profile";
        }
        if(!Util.isAnonymous()){
            model.addAttribute("currentUser", userService.getCurrentUser());
        }
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("view", "/user/details");
            return "base-layout";
        }
        return "redirect:/error/404";
    }
}
