package com.example.load.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoadAppController {

    @GetMapping
    public String index() {
        return "index";
    }

    @PostMapping("/submit")
    public String submit() {
        return "redirect:/result?id=" + System.currentTimeMillis();
    }

    @GetMapping("/result")
    public String result(@RequestParam String id, Model model) {
        model.addAttribute("currentTimeMillis", id);
        return "result";
    }
}
