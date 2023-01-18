package com.example.validation;

import jakarta.validation.constraints.Email;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExampleController {

    @GetMapping("/")
    public String main() {
        return "example";
    }

    record Form(
            @Email String email) {
    }

    @PostMapping("/check")
    public String check(Form form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "example";
        }
        return "redirect:/valid";
    }

    @GetMapping("/valid")
    public String valid() {
        return "valid";
    }
}
