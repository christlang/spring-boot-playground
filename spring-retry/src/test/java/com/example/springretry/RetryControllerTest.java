package com.example.springretry;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class RetryControllerTest {

    @Configuration
    @EnableRetry
    @Import({RetryController.class, ShakyService.class, ShakyServiceRetry.class})
    public static class MyConfig {

    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                post("/shaky-service-retry")
                        .param("successRate", "3"))
                .andExpect(status().isOk())
                .andExpect(view().name("retry"))
                .andExpect(content().string(containsString("counter value: 3")));

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
        assertThat(mv.getViewName()).isEqualTo("retry");
    }
}
