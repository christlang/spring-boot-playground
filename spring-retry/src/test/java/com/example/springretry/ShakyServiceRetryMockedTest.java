package com.example.springretry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.retry.annotation.EnableRetry;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@EnableRetry
@ExtendWith(MockitoExtension.class)
class ShakyServiceRetryMockedTest {

    @Mock
    ShakyService shakyService;

    @InjectMocks
    ShakyServiceRetry cut;

    @Test
    void testSuccess() {
        cut.doRequest(3);
    }

    @Test
    void testFailed() {
        when(shakyService.doRequest(4)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () ->
                cut.doRequest(4)
        );
    }
}