package com.AGag.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {
    @GetMapping("/404")
    public String err404(Model model){

        model.addAttribute("view", "/error/404");
        return "base-layout";
    }

    @GetMapping("/403")
    public String err403(Model model){

        model.addAttribute("view", "/error/403");
        return "base-layout";
    }
}
