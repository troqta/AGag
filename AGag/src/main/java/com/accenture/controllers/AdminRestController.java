package com.accenture.controllers;

import com.accenture.services.Base.GagService;
import com.accenture.services.Base.UserService;
import com.accenture.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api")
public class AdminRestController {

    private UserService userService;

    private GagService gagService;

    @Autowired
    public AdminRestController(UserService userService, GagService gagService) {
        this.userService = userService;
        this.gagService = gagService;
    }

    @PostMapping("/user/ban/{username}")
    public String banUser(@PathVariable String username) {

        return userService.ban(username);
    }

    @PostMapping("/user/unban/{username}")
    public String unBanUser(@PathVariable String username) {

        return userService.unBan(username);
    }
}
