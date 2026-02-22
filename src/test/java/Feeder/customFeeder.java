package Feeder;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class customFeeder extends Simulation {

    // 1 HTTP Configuration
    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    private static ChainBuilder authenticate =
            exec(http("Post Request of Authentication")
                    .post("/authenticate")
                    .body(StringBody("{\n" +
                            "    \"username\": \"admin\",\n" +
                            "    \"password\": \"admin\"\n" +
                            "}"))
                    .check(status().is(200))
                    .check(jmesPath("token").saveAs("authToken")));

    private static Iterator<Map<String,Object>> customFeeder =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                Random random = new Random();
                int gameId = random.nextInt(10 - 1 + 1) + 1;
                return Collections.singletonMap("gameId",gameId);
            }
            ).iterator();

    private static ChainBuilder getSpecficVideGame =
            feed(customFeeder)
                    .exec(http("Get video game with id - #{gameId}")
                    .get("/videogame/#{gameId}")
                            .header("Authorization","Bearer #{authToken}")
                    .check(status().is(200)));

    // 2 Scenario Definition
    private ScenarioBuilder scn = scenario("Section 6 Code")
            .exec(authenticate)
            .repeat(10).on(
                    exec(getSpecficVideGame)
                            .pause(1)
            );


    // 3 Load Simulation
    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }

}