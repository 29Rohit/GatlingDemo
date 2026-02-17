package DemoTest;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class VideoGame_Repeat extends Simulation {
    // 1 HTTP Configuration
    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    private static ChainBuilder getAllVideGames =
            repeat(3).on(
                    exec(http("Get all games")
                    .get("/videogame")
                    .check(status().not(400),status().not(500))));

    private static ChainBuilder getSpecficVideGame =
            repeat(5,"MyCounter").on(
                    exec(http("Get specfic video game with id: #{MyCounter}")
                    .get("/videogame/1")
                    .check(status().is(200))));


    // 2 Scenario Definition
    private ScenarioBuilder scn = scenario("Video Game code - Section 5 Code")
            .exec(getAllVideGames)
            .pause(5)
            .exec(getSpecficVideGame)
            .pause(5)
            .repeat(2).on(
            exec(getAllVideGames)
            );

    // 3 Load Simulation
    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }
}
