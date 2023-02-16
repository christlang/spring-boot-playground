package com.example.springoauthjwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.webjars.NotFoundException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringOauthJwtApplicationTests {

	@LocalServerPort
	private Integer port;
	private final ObjectMapper objectMapper = new ObjectMapper();

	private final User hasRight = new User("mandy", "mandy");
	private final User hasNotRight = new User("joe", "joe");

	@Test
	void checkCorrectRight() throws JsonProcessingException {
		final var result = requestGreet(hasRight);

		assertThat(result.getStatusCode().value()).isEqualTo(200);
		assertThat(result.getBody()).isEqualTo(
				"Hi %s! You are granted with: [NICE].".formatted(hasRight.name));
	}

	@Test
	void checkPermissionDenied() throws JsonProcessingException {
		final var result = requestGreet(hasNotRight);

		assertThat(result.getStatusCode().value()).isEqualTo(403);
	}

	ResponseEntity<String> requestGreet(User user) throws JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());

		final var headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);
		headers.setBearerAuth(getAccessToken(user));

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

		return restTemplate.exchange("http://localhost:%s/greet".formatted(port),
				HttpMethod.GET, request, String.class);
	}

	record User(String name, String pass) {}

	private String getAccessToken(User user) throws JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();

		final var headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("grant_type", "password");
		form.add("client_id", "spring-boot");
		form.add("client_secret", "aweLEwtZ5H5rLVOQPQx2abuFocHtEv0v");
		form.add("username", user.name);
		form.add("password", user.pass);

		HttpEntity<MultiValueMap<String, String>> request =
				new HttpEntity<>(form, headers);

		final var result = restTemplate.exchange("http://localhost:8081/realms/example/protocol/openid-connect/token",
				HttpMethod.POST, request, String.class);

		assertThat(result.getStatusCode().value()).isEqualTo(200);

		JsonNode root = objectMapper.readTree(result.getBody());
		String accessToken = root.get("access_token").textValue();

		assertThat(accessToken).isNotNull();

		return accessToken;
	}

	static class RestTemplateResponseErrorHandler
			implements ResponseErrorHandler {

		@Override
		public boolean hasError(ClientHttpResponse httpResponse)
				throws IOException {

			return (
					httpResponse.getStatusCode().is4xxClientError()
							|| httpResponse.getStatusCode().is5xxServerError());
		}

		@Override
		public void handleError(ClientHttpResponse httpResponse)
				throws IOException {

			if (httpResponse.getStatusCode().is5xxServerError()) {
				// handle SERVER_ERROR
			} else if (httpResponse.getStatusCode().is4xxClientError()) {
				// handle CLIENT_ERROR
				if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
					throw new NotFoundException("not found");
				}
			}
		}
	}

}
