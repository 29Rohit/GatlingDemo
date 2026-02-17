package DemoTest;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class VideoGame_Methods extends Simulation {
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


    private static ChainBuilder createGame =
            exec(http("Create Game")
                    .post("/videogame")
                    .header("Authorization","Bearer #{authToken}")
                    .body(StringBody("{\n" +
                            "  \"category\": \"Platform\",\n" +
                            "  \"name\": \"Mario\",\n" +
                            "  \"rating\": \"Mature\",\n" +
                            "  \"releaseDate\": \"2012-05-04\",\n" +
                            "  \"reviewScore\": 85\n" +
                            "}"))
                    .check(status().is(200))
                    .check(bodyString().saveAs("responseBody")))
                    .exec(session -> {
                                System.out.println("Response Body " +session.getString("responseBody"));
                                return session;
                    }
                    );

    private static ChainBuilder getAllVideGames =
            exec(http("Get all games")
                            .get("/videogame")
                            .check(status().not(400),status().not(500)));

    private static ChainBuilder getSpecficVideGame =
            exec(http("Get specfic video game")
                    .get("/videogame/1")
                    .check(status().is(200)));


    // 2 Scenario Definition
    private ScenarioBuilder scn = scenario("Video Game code - Section 5 Code")
            .exec(authenticate)
            .exec(createGame);

    // 3 Load Simulation
    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }
}
