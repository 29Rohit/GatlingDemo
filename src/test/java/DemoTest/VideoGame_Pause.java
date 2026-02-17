package DemoTest;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;
import java.util.List;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class VideoGame_Pause extends Simulation {
    // 1 HTTP Configuration
    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    // 2 Scenario Definition
    private ScenarioBuilder scn = scenario("Video Game code - Pause")
            .exec(http("Get all games - 1 call")
                    .get("/videogame")
                    .check(status().is(200)) //Verify the status code is 200
//                    .check(jsonPath("$[?(@.id==3)].name").is("Tetris"))) // Verify if the response body json contains value "Tetris" for key "name" in array index  of id contain 3
                    .check(jmesPath("[? id == `1`].name").ofList().is(List.of("Resident Evil 4")))) //Verify if the response body json contains value "Resident Evil 4" for key "name" in array index of id contain 1
            .pause(5)
            .exec(http("Get specific game")
                    .get("/videogame/3")
                    .check(status().in(200,201,202))) // Verify if the status code is one of this status code
            .pause(1,10)
            .exec(http("Get all games - 2 call")
                    .get("/videogame")
                    .check(status().not(400),status().not(500))); // Verify if the status code is not one of this status code(400/500)
//            .pause(Duration.ofSeconds(3000));
    // 3 Load Simulation
    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }
}
