package com.accenture.controllers;

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
}
