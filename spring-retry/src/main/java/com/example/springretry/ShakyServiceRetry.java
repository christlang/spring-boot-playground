package com.example.springretry;

import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class ShakyServiceRetry {

    final ShakyService shakyService;

    public ShakyServiceRetry(ShakyService shakyService) {
        this.shakyService = shakyService;
    }

    @Retryable
    public String doRequest(int successRate) {
        return shakyService.doRequest(successRate);
    }
}
