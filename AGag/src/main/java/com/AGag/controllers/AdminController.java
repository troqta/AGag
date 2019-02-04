package com.AGag.controllers;

import com.AGag.entities.User;
import com.AGag.services.Base.GagService;
import com.AGag.services.Base.UserService;
import com.AGag.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;

    private GagService gagService;

    @Autowired
    public AdminController(UserService userService, GagService gagService) {
        this.userService = userService;
        this.gagService = gagService;
    }

    @GetMapping("/users")
    public String viewAllUsers(Model model) {

        List<User> users = userService.getAll();

        model.addAttribute("users", users);
        model.addAttribute("view", "/admin/users");
        return "base-layout";
    }

    @PostMapping("/user/ban/{username}")
    public String banUser(@PathVariable String username) {

        if (Util.isAnonymous() || !userService.getCurrentUser().isAdmin()) {
            return "redirect:/error/403";
        }
        userService.ban(username);

        return "redirect:/admin/users";
    }

    @PostMapping("/user/unban/{username}")
    public String unBanUser(@PathVariable String username) {

        if (Util.isAnonymous() || !userService.getCurrentUser().isAdmin()) {
            return "redirect:/error/403";
        }
        userService.unBan(username);

        return "redirect:/admin/users";
    }
}
