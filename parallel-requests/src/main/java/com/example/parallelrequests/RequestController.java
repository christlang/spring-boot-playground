package com.example.parallelrequests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RequestController {

    Logger log = LoggerFactory.getLogger(RequestController.class);

    static final String URL = "https://www.google.de/";

    @GetMapping
    String index() {
        return "index";
    }

    @GetMapping("webclient")
    String webclient(@RequestParam("calls") Integer calls, Model model) {
        model.addAttribute("duration", 10);

        WebClient webClient = WebClient.create(URL);

        long start = System.currentTimeMillis();

        List<String> requestUrls = new ArrayList<>();

        for (int i=0; i < calls; ++i) {
            requestUrls.add(URL);
        }

        final Flux<String> stringFlux = Flux.fromIterable(requestUrls)
                .flatMap(url ->
                        webClient
                                .get()
                                .exchangeToMono(response -> Mono.just("result %s".formatted(response.statusCode())))
                );

        List<String> results = stringFlux.toStream().toList();

        long end = System.currentTimeMillis();
        long diffInSeconds = end-start;
        log.info("took %s ms".formatted(diffInSeconds));
        model.addAttribute("duration", diffInSeconds);
        model.addAttribute("results", results);

        return "client-result";
    }

    @GetMapping("resttemplate")
    String restclient(@RequestParam("calls") Integer calls, Model model) {
        RestTemplate rest = new RestTemplate();
        List<String> results = new ArrayList<>();

        long start = System.currentTimeMillis();

        for (int i=0; i < calls; ++i) {
            results.add("result " + rest.getForEntity(URL, String.class).getStatusCode());
        }

        long end = System.currentTimeMillis();
        long diffInSeconds = end-start;
        log.info("took %s ms".formatted(diffInSeconds));
        model.addAttribute("duration", diffInSeconds);
        model.addAttribute("results", results);
        return "client-result";
    }
}
