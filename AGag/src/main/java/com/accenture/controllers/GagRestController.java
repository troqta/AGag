package com.accenture.controllers;

import com.accenture.services.Base.GagService;
import com.accenture.services.Base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/like/{id}")
    public String like(@PathVariable int id){
        return gagService.likeRest(id);
    }

    @GetMapping("/exists/{username}")
    public String test(@PathVariable String username){
        System.out.println("im in api gag");
        return userService.checkIfUserExists(username);
    }
}
