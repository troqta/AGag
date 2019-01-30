package com.accenture.controllers;

import com.accenture.services.Base.GagService;
import com.accenture.services.Base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gag")
public class GagRestController {

    private GagService gagService;

    private UserService userService;

    @Autowired
    public GagRestController(GagService gagService, UserService userService) {
        this.gagService = gagService;
        this.userService = userService;
    }
    @GetMapping("/fresh/{number}")
    public String fresh(@PathVariable int number){
        return gagService.getFresh(number);
    }
    @GetMapping("/hot/{number}")
    public String hot(@PathVariable int number){
        return gagService.getHot(number);
    }
}
