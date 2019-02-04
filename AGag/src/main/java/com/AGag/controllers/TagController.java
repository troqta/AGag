package com.AGag.controllers;

import com.AGag.entities.Gag;
import com.AGag.entities.Tag;
import com.AGag.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tag")
public class TagController {

    private TagRepository tagRepository;

    @Autowired
    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping("/{tag}")
    public String viewTag(Model model, @PathVariable String tag){
        Tag tagEntity = tagRepository.findByName(tag);
        if(tagEntity == null){
            return "redirect:/error/404";
        }
        List<Gag> gags = tagEntity.getTaggedGags();

        model.addAttribute("tag", tag);
        model.addAttribute("gags", gags);
        model.addAttribute("view", "/tag/details");

        return "base-layout";
    }
}
