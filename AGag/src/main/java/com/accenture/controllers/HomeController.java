package com.accenture.controllers;

import com.accenture.entities.BindingModels.UserBindingModel;
import com.accenture.entities.User;
import com.accenture.services.Base.UserService;
import com.accenture.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class HomeController {

    private UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model){
        if (!Util.isAnonymous()){
            User user = userService.getCurrentUser();
            model.addAttribute("user", user);
        }
        model.addAttribute("view", "home");

        return "base-layout";
    }
    @GetMapping("/register")
    public String register(Model model) {
        if (!Util.isAnonymous()) {
            return "redirect:/";
        }
        model.addAttribute("view", "register");
        model.addAttribute("user", new UserBindingModel());
        return "base-layout";
    }


    @PostMapping("/register")
    public String registerPost(@Valid UserBindingModel user, BindingResult errors, Model model){
        if (!userService.registerUser(user, errors) || errors.hasErrors()) {
            model.addAttribute("errors", errors.getAllErrors());
            model.addAttribute("view", "register");

            return "base-layout";
        }



        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("view", "login");
        return "base-layout";
    }
}
