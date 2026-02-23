package LoadSimulation;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class basicSimulation extends Simulation  {

    // 1 HTTP Configuration
    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    private static final int USER_COUNT = Integer.parseInt(System.getProperty("USERS", "5"));
    private static final int RAMP_DURATION = Integer.parseInt(System.getProperty("RAMP_DURATION", "10"));
    private static final int TEST_DURATION = Integer.parseInt(System.getProperty("TEST_DURATION", "20"));

    @Override //This function will run at start of load simulation
    public void before(){
        System.out.printf("Running test with %d users%n",USER_COUNT);
        System.out.printf("Ramping users over with %d seconds%n",RAMP_DURATION);
        System.out.printf("Total test duration %d seconds%n",TEST_DURATION);
    }
    private static ChainBuilder authenticate =
            exec(http("Post Request of Authentication")
                    .post("/authenticate")
                    .body(StringBody("{\n" +
                            "    \"username\": \"admin\",\n" +
                            "    \"password\": \"admin\"\n" +
                            "}"))
                    .check(status().is(200))
                    .check(jmesPath("token").saveAs("authToken")));

    private static ChainBuilder getAllVideGames =
            exec(http("Get all games")
                    .get("/videogame")
                    .header("Authorization","Bearer #{authToken}")
                    .check(status().not(400),status().not(500)));

    private static ChainBuilder getSpecficVideGame =
            exec(http("Get specific video game")
                    .get("/videogame/1")
                    .header("Authorization","Bearer #{authToken}")
                    .check(status().is(200)));

    private ScenarioBuilder scn = scenario("Section 7 Code")
            .exec(authenticate)
            .pause(5)
            .exec(getAllVideGames)
            .pause(5)
            .exec(getSpecficVideGame);

    // 3 Load Simulation
    {
        setUp(
                scn.injectOpen(     //injectOpen() defines how users are injected
                        nothingFor(4), //Pause for a given duration.
//                        atOnceUsers(10), // Injects a given number of users at once.(Starts all 10 users immediately at the same time)
                        rampUsers(USER_COUNT).during(RAMP_DURATION), //Gradually start 10 users over 20 seconds
                        constantUsersPerSec(USER_COUNT).during(RAMP_DURATION) //Start 5 users per second, continuously, for 10 seconds ie 50 users injected
                )
        ).protocols(httpProtocol); //protocols() defines how requests behave
    }

}
