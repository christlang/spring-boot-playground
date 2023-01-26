package com.example.springretry;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.annotation.EnableRetry;

import static org.junit.jupiter.api.Assertions.assertThrows;

@EnableRetry
@SpringBootTest(classes = {ShakyServiceRetry.class, ShakyService.class})
class ShakyServiceRetryTest {

    @Autowired
    ShakyServiceRetry cut;

    @Test
    void testSuccess() {
        cut.doRequest(3);
    }

    @Test
    void testFailed() {
        assertThrows(IllegalArgumentException.class, () ->
                cut.doRequest(4)
        );
    }
}