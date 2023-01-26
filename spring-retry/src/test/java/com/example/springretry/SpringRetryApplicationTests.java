package com.example.springretry;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class SpringRetryApplicationTests {

	@MockBean
	private ShakyService shakyService;

	@Autowired
	private ShakyServiceRetry shakyServiceRetry;

	@Test
	void contextLoads() {
		when(shakyService.doRequest(3))
				.thenThrow(IllegalArgumentException.class)
				.thenThrow(IllegalArgumentException.class)
				.thenReturn("done");

		assertThat(shakyServiceRetry.doRequest(3)).isEqualTo("done");
	}

}
