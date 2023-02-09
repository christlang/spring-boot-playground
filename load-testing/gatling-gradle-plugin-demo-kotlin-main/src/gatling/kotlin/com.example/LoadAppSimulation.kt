package com.example

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl

class LoadAppSimulation : Simulation() {

    private val httpProtocol = HttpDsl.http
        .baseUrl("http://localhost:8080")
        .acceptHeader("text/html")

    private val scn = CoreDsl.scenario("produce some load")
        .forever().on(
            CoreDsl.exec(getIndex)
                .pause(5)
                .exec(getIndex)
        )

    init {
        setUp(
            scn.injectOpen(
                CoreDsl.nothingFor(5),
                CoreDsl.rampUsers(10000).during(60)
            ).protocols(httpProtocol)
        ).maxDuration(60)
    }

    companion object {
        private val getIndex = CoreDsl.exec(HttpDsl.http("get index")[""])
    }
}