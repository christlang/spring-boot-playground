package com.example.springretry;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RetryController {

    final ShakyService shakyService;
    final ShakyServiceRetry shakyServiceRetry;

    public RetryController(ShakyService shakyService, ShakyServiceRetry shakyServiceRetry) {
        this.shakyService = shakyService;
        this.shakyServiceRetry = shakyServiceRetry;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/shaky-service")
    public String shakyService(@RequestParam int successRate, Model model) {
        model.addAttribute("message", shakyService.doRequest(successRate));
        return "retry";
    }

    @PostMapping("/shaky-service-retry")
    public String shakyServiceRetry(@RequestParam int successRate, Model model) {
        model.addAttribute("message", shakyServiceRetry.doRequest(successRate));
        return "retry";
    }
}
