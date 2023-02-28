package com.example;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class LoadAppTesting extends Simulation {

    final private HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("text/html");


    final private static ChainBuilder getIndex =
            exec(http("get index")
                    .get(""));

    final private static ChainBuilder post =
            exec(http("submit")
                    .post("/submit")
                    .check(currentLocation().saveAs("responseUrl"))
            );

    final private static ChainBuilder getResult =
            exec(http("get response #{responseUrl}")
                    .get("#{responseUrl}")
                    .check(substring("result: <span>1</span>").find().exists())
            );



    final private ScenarioBuilder scn = scenario("produce some load")
            .forever().on(
                    exec(getIndex)
                            .pause(5)
                            .exec(getIndex)
                            .pause(5)
                            .exec(post)
                            .pause(Duration.ofMillis(10))
                            .exec(
                                    session -> {
                                        String before = session.get("responseUrl").toString();
                                        String after = before.replaceAll("id=\\d+", "id=1");
                                        session.remove("responseUrl");
                                        // System.out.println("responseUrl before: " + before);
                                        // System.out.println("responseUrl after : " + after);
                                        return session.set("responseUrl", after);
                                    }
                            )
                            .exec(getResult)
            );


    {
        setUp(
                scn.injectOpen(
                        nothingFor(5),
                        rampUsers(100_000).during(60)
                ).protocols(httpProtocol)
        ).maxDuration(60);
    }
}
