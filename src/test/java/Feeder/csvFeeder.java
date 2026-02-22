package Feeder;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class csvFeeder extends Simulation {

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

    private static FeederBuilder.FileBased<String> csvFeeder = csv("data/gameCsvFile.csv").circular();

    private static ChainBuilder getSpecficVideGame =
            feed(csvFeeder)
                    .exec(http("Get video game with name - #{gameName}")
                    .get("/videogame/#{gameId}")
                            .header("Authorization","Bearer #{authToken}")
                    .check(status().is(200))
                            .check(jmesPath("name").isEL("#{gameName}")));

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