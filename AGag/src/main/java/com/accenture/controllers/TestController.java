package com.accenture.controllers;

import com.accenture.custom.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

//    @PostMapping("upload")
//    public String handleUpload(Model model, @RequestParam("file")MultipartFile file){
//
//        storage.init();
//        storage.storeWithCustomLocation("test", file);
//
//
//        return "redirect:/";
//    }
}
