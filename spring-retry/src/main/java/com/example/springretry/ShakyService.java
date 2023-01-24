package com.example.springretry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ShakyService {

    final static Logger log = LoggerFactory.getLogger(ShakyService.class.getName());

    volatile AtomicInteger counter = new AtomicInteger();

    public String doRequest(int successRate) {
        if (counter.incrementAndGet() % successRate > 0) {
            log.warn("self-healing request");
            throw new IllegalArgumentException("counter value %s can not be divided %s without rest".formatted(counter, successRate));
        }

        return "counter value: " + counter;
    }
}
