package DemoTest;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import java.time.Duration;
import java.util.List;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class VideoGame_SessionVariable extends Simulation {
    // 1 HTTP Configuration
    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    // 2 Scenario Definition
    private ScenarioBuilder scn = scenario("Video Game code - Pause")
            .exec(http("Get specific game")
                    .get("/videogame/1")
                    .check(status().is(200))//Verify the status code is 200
                    .check(jmesPath("name").is("Resident Evil 4")))
            .pause(1,10)

            .exec(http("Get all games")
                    .get("/videogame")
                    .check(status().not(400),status().not(500))
                    .check(jmesPath("[4].id").saveAs("gamerID")))
            .exec(
                    session -> {
                        System.out.println(session);
                        System.out.println("gamerID set to: " +session.getString("gamerID"));
                        return session;
                    }
            )
            .pause(4)

            .exec(http("Get specific game with gamerID - #{gamerID}")
                            .get("/videogame/#{gamerID}")
                            .check(status().not(400),status().not(500))
                            .check(jmesPath("name").is("The Legend of Zelda: Ocarina of Time"))
                            .check(bodyString().saveAs("responseBody")))
            .exec(
                    session -> {
                        System.out.println("Response Body " +session.getString("responseBody"));
                        return session;
                    }
            );

    // 3 Load Simulation
    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }
}
