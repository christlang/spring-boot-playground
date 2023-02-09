package com.example.load.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoadAppController {

    @GetMapping
    public String index() {
        return "index";
    }
}
