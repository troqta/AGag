package com.AGag.controllers;

import com.AGag.services.Base.GagService;
import com.AGag.services.Base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private GagService gagService;

    private UserService userService;

    @Autowired
    public UserRestController(GagService gagService, UserService userService) {
        this.gagService = gagService;
        this.userService = userService;
    }
    @GetMapping("/exists/{username}")
    public String checkIfUserExists(@PathVariable String username){
        return userService.checkIfUserExists(username);
    }
    @GetMapping("/test")
    public String test(@PathVariable String username){
        return "hello";
    }
}
