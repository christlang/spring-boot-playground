package com.example.springoauthjwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringOauthJwtApplicationTests {

	@LocalServerPort
	private Integer port;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void contextLoads() throws JSONException, JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();

		final var headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);
		headers.setBearerAuth(getAccessToken());

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

		final var result = restTemplate.exchange("http://localhost:%s/greet".formatted(port),
				HttpMethod.GET, request, String.class);

		assertThat(result.getStatusCode().value()).isEqualTo(200);
		assertThat(result.getBody()).isEqualTo(
				"Hi brice! You are granted with: [offline_access, NICE, default-roles-example, uma_authorization].");
	}


	private String getAccessToken() throws JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();

		final var headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("grant_type", "password");
		form.add("client_id", "spring-boot");
		form.add("client_secret", "aweLEwtZ5H5rLVOQPQx2abuFocHtEv0v");
		form.add("username", "brice");
		form.add("password", "brice");

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
}
