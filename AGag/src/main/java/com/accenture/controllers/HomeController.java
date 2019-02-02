package com.accenture.controllers;

import com.accenture.custom.PathProperty;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class HomeController {

    private UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }
        @Autowired
    private PathProperty pathProperty;

    @GetMapping("/")
    public String home(Model model){
        if (!Util.isAnonymous()){
            User user = userService.getCurrentUser();
            model.addAttribute("user", user);
        }
        System.out.println("PROPERTY = " + pathProperty.getPath());
        model.addAttribute("view", "home");

//        return "base-layout";
        return "redirect:/gag/hot";
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
    public String login(Model model,  @RequestParam(required = false) String error){
        if (error != null){
            model.addAttribute("error", Util.BAD_CREDENTIALS_MESSAGE);
        }
        model.addAttribute("view", "login");
        return "base-layout";
    }
}
