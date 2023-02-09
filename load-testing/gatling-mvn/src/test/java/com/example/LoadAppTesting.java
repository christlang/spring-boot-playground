package com.example;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class LoadAppTesting extends Simulation {

    final private HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("text/html");


    final private static ChainBuilder getIndex =
            exec(http("get index")
                    .get(""));


    final private ScenarioBuilder scn = scenario("produce some load")
            .forever().on(
                    exec(getIndex)
                            .pause(5)
                            .exec(getIndex)
            );


    {
        setUp(
                scn.injectOpen(
                        nothingFor(5),
                        rampUsers(10_000).during(60)
                ).protocols(httpProtocol)
        ).maxDuration(60);
    }
}
