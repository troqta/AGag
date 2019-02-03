package com.accenture.controllers;

import com.accenture.entities.BindingModels.CommentBindingModel;
import com.accenture.entities.BindingModels.GagBindingModel;
import com.accenture.entities.Gag;
import com.accenture.entities.User;
import com.accenture.services.Base.GagService;
import com.accenture.utils.Util;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/gag")
public class GagController {


    private GagService gagService;

    @Autowired
    public GagController(GagService gagService) {
        this.gagService = gagService;
    }

    @RequestMapping("/{id}")
    public String viewGag(Model model, @PathVariable int id) {
        Gag gag = gagService.getById(id);
        if (gag == null) {
            return "redirect:/error/404";
        }
        boolean hasLiked = false;
        User user = gagService.getCurrentUser();
        if (!Util.isAnonymous()) {
            hasLiked = user.getLikedGags().contains(gag);
            model.addAttribute("user", user);
        }
        model.addAttribute("hasLiked", hasLiked);
        model.addAttribute("gag", gag);
        model.addAttribute("view", "/gag/details");

        return "base-layout";
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        if(Util.isAnonymous())
            return "redirect:/error/403";
        model.addAttribute("view", "/gag/create");

        return "base-layout";
    }

    @PostMapping("/create")
    public String createGag(@RequestParam("file") MultipartFile file, GagBindingModel model) {
        if(Util.isAnonymous())
            return "redirect:/error/403";
        if (!gagService.createGag(model, file)) {
            return "redirect:/gag/create";
        }
        return "redirect:/";
    }

    @GetMapping("/fresh")
    public String fresh(Model model) {
        List<Gag> gags = gagService.getAll().stream()
                .sorted(Comparator.comparing(Gag::getCreatedOn).reversed())
                .limit(Util.GAGS_PER_PAGE)
                .collect(Collectors.toList());


        model.addAttribute("view", "/gag/all");
        model.addAttribute("gags", gags);

        return "base-layout";
    }

    @PostMapping("/like/{id}")
    public String likePost(@PathVariable int id) {

        gagService.likeById(id);

        return "redirect:/gag/" + id;
    }

    @GetMapping("/hot")
    public String hot(Model model) {
        List<Gag> gags = gagService.getAll().stream()
                .sorted(Comparator.comparing(Gag::getUpvotes).reversed())
                .limit(Util.GAGS_PER_PAGE)
                .collect(Collectors.toList());


        model.addAttribute("view", "/gag/hot");
        model.addAttribute("gags", gags);

        return "base-layout";
    }
    @PostMapping("/comment/{id}")
    public String postComment(@PathVariable int id, @Valid CommentBindingModel model){
        if (Util.isAnonymous()){
            return "redirect:/error/403";
        }
        gagService.postComment(id, model);
        return "redirect:/gag/" + id;
    }

    @GetMapping("/edit/{id}")
    public String editGag(@PathVariable int id, Model model){
        if(Util.isAnonymous()){
            return "redirect:/error/403";
        }
        Gag gag = gagService.getById(id);

        if(gag == null){
            return "redirect:/error/404";

        }
        User user = gagService.getCurrentUser();
        if (!user.isAdmin()){
            if (!user.isAuthor(gag)){
                return "redirect:/error/403";
            }
        }
        model.addAttribute("gag", gag);
        model.addAttribute("view", "/gag/edit");

        return "base-layout";
    }

    @PostMapping("/edit/{id}")
    public String handleEditGag(@PathVariable int id, @RequestParam("file") MultipartFile file){
        if (Util.isAnonymous()){
            return "redirect:/error/403";
        }
        Gag gag = gagService.getById(id);
        if (gag == null){
            return "redirect:/error/404";
        }
        User user = gagService.getCurrentUser();
        if (!user.isAdmin()){
            if (!user.isAuthor(gag)){
                return "redirect:/error/403";
            }
        }
        gagService.editGag(id, file);
        return "redirect:/gag/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id){
        if (!gagService.deleteGag(id)){
            return "redirect:/error/403";
        }
        return "redirect:/gag/hot";
    }
}
