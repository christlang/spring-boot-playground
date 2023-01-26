package com.example.springretry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class ShakyServiceRetry {

    final static Logger log = LoggerFactory.getLogger(ShakyServiceRetry.class.getName());

    final ShakyService shakyService;

    public ShakyServiceRetry(ShakyService shakyService) {
        this.shakyService = shakyService;
    }

    @Retryable
    public String doRequest(int successRate) {
        log.info("doRequest");
        return shakyService.doRequest(successRate);
    }
}
