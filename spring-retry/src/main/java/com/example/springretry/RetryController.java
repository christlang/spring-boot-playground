package com.example.springretry;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/shaky-service")
    public String shakyService(Model model) {
        model.addAttribute("message", shakyService.doRequest(3));
        return "retry";
    }

    @GetMapping("/shaky-service-retry")
    public String shakyServiceRetry(Model model) {
        model.addAttribute("message", shakyServiceRetry.doRequest(3));
        return "retry";
    }
}
