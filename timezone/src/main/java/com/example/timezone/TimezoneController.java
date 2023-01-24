package com.example.timezone;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.TimeZone;

@Controller
public class TimezoneController {

    @GetMapping("/")
    public String main(TimeZone requestTimezone, Model model) {
        model.addAttribute("formattedTime", "");
        model.addAttribute("requestTimezone", requestTimezone.getDisplayName());
        return "main";
    }
}
