package com.example.parallelrequests;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RequestControllerTest     {

    @LocalServerPort
    int randomServerPort;

    static final WireMockServer wm = new WireMockServer();

    static {
        wm.start();
    }


    @BeforeEach
    void beforeEach() {
        wm.resetAll();

        wm.stubFor(get(urlEqualTo(RequestController.URL)).willReturn(aResponse().withFixedDelay(1)
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("ignore")));
    }

    @Test
    public void requestRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        final ResponseEntity<String> forEntity1 = restTemplate.getForEntity(RequestController.URL, String.class);
        System.out.println(forEntity1.getBody());

        final ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:%s/resttemplate?calls=1".formatted(randomServerPort), String.class);

        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(forEntity.getBody()).containsOnlyOnce("<span>result 200 OK</span>");
    }

    @Test
    public void requestWebclient() {
        RestTemplate restTemplate = new RestTemplate();

        final ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:%s/webclient?calls=1".formatted(randomServerPort), String.class);

        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(forEntity.getBody()).containsOnlyOnce("<span>result 200 OK</span>");
    }


    @Test
    public void requestRestTemplateMultiple() {
        RestTemplate restTemplate = new RestTemplate();

        final ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:%s/resttemplate?calls=30".formatted(randomServerPort), String.class);

        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(forEntity.getBody()).contains("<span>result 200 OK</span>");
    }

    @Test
    public void requestWebclientMultiple() {
        RestTemplate restTemplate = new RestTemplate();

        final ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:%s/webclient?calls=30".formatted(randomServerPort), String.class);

        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(forEntity.getBody()).contains("<span>result 200 OK</span>");
    }

//    @Test
//    public void givenClient_whenFetchingUsers_thenExecutionTimeIsLessThanDouble() {
//
//        int requestsNumber = 5;
//        int singleRequestTime = 1000;
//
//        for (int i = 1; i <= requestsNumber; i++) {
//            stubFor(get(urlEqualTo(RequestController.URL)).willReturn(aResponse().withFixedDelay(singleRequestTime)
//                    .withStatus(200)
//                    .withHeader("Content-Type", "application/json")
//                    .withBody(String.format("{ \"id\": %d }", i))));
//        }
//
//        List<Integer> userIds = IntStream.rangeClosed(1, requestsNumber)
//                .boxed()
//                .collect(Collectors.toList());
//
//        long start = System.currentTimeMillis();
//        List<User> users = client.fetchUsers(userIds).collectList().block();
//        long end = System.currentTimeMillis();
//
//        long totalExecutionTime = end - start;
//
//        assertEquals("Unexpected number of users", requestsNumber, users.size());
//        assertTrue("Execution time is too big", 2 * singleRequestTime > totalExecutionTime);
//    }

}