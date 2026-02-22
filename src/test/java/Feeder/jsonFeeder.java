package Feeder;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class jsonFeeder extends Simulation {

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

    private static FeederBuilder.FileBased<Object> jsonFeeder = jsonFile("data/gameJsonFile.json").circular();

    private static ChainBuilder getSpecficVideGame =
            feed(jsonFeeder)
                    .exec(http("Get video game with name - #{name}")
                    .get("/videogame/#{id}")
                            .header("Authorization","Bearer #{authToken}")
                    .check(status().is(200))
                            .check(jmesPath("name").isEL("#{name}")));

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