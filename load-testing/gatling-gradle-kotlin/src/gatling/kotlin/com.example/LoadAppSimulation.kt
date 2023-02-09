package com.example

import io.gatling.javaapi.core.*
import io.gatling.javaapi.http.*
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*

class LoadAppSimulation : Simulation() {

    private val httpProtocol =
        http
            .baseUrl("http://localhost:8080")
            .acceptHeader("text/html")

    private val getIndex = exec(http("get index").get(""))

    private val scn = scenario("produce some load")
        .forever().on(
            exec(getIndex)
                .pause(5)
                .exec(getIndex)
        )

    init {
        setUp(
            scn.injectOpen(
                nothingFor(5),
                rampUsers(10000).during(60)
            ).protocols(httpProtocol)
        ).maxDuration(60)
    }
}