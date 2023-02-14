package com.example.springoauthjwt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExampleController {

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/secure")
    public String secure() {
        return "secure";
    }

}
