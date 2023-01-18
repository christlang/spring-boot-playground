package com.example.validation;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.regex.Pattern;

@Controller
public class ExampleController {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("form", new Form(""));
        return "example";
    }

    record Form(
            String email) {
    }

    @PostMapping("/check")
    public String check(@Valid Form form, BindingResult bindingResult, Model model) {

        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(form.email).find()) {
            bindingResult.addError(new ObjectError("email", "email not valid"));
            model.addAttribute("errors", new ObjectError("email", "email not valid"));
            return "example";
        }
        return "redirect:/valid?validInput=" + form.email;
    }

    @GetMapping("/valid")
    public String valid(@RequestParam("validInput") String validInput, Model model) {
        model.addAttribute("validInput", validInput);
        return "valid";
    }
}
