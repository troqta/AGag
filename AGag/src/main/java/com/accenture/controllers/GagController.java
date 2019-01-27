package com.accenture.controllers;

import com.accenture.entities.BindingModels.GagBindingModel;
import com.accenture.entities.Gag;
import com.accenture.services.Base.GagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/gag")
public class GagController {


    private GagService gagService;

    @Autowired
    public GagController(GagService gagService) {
        this.gagService = gagService;
    }
    @RequestMapping("/{id}")
    public String viewGag(Model model, @PathVariable int id){
        Gag gag = gagService.getById(id);
        model.addAttribute("gag", gag);
        model.addAttribute("view", "/gag/details");

        return "base-layout";
    }

    @GetMapping("/create")
    public String createPage(Model model){

        model.addAttribute("view", "/gag/create");

        return "base-layout";
    }

    @PostMapping("/create")
    public String createGag(@RequestParam("file")MultipartFile file, GagBindingModel model){
        if (!gagService.createGag(model, file)){
            return "redirect:/gag/create";
        }
        return "redirect:/";
    }

    @GetMapping("/all")
    public String all(Model model){
        List<Gag> gags = gagService.getAll();

        model.addAttribute("view","/gag/all");
        model.addAttribute("gags",gags);

        return "base-layout";
    }
}
