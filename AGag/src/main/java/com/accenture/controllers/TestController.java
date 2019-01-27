package com.accenture.controllers;

import com.accenture.services.Base.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/test")
public class TestController {

    private Storage storage;

    @Autowired
    public TestController(Storage storage){
        this.storage = storage;
    }

    @GetMapping("upload")
    public String uploadPage(Model model){

        model.addAttribute("view", "test");


        return "base-layout";
    }

    @PostMapping("upload")
    public String handleUpload(Model model, @RequestParam("file")MultipartFile file){

        storage.init();
        storage.storeWithCustomLocation("test", file);


        return "redirect:/";
    }
}
