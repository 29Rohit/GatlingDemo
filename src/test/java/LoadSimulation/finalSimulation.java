package LoadSimulation;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class finalSimulation extends Simulation {
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
    private static FeederBuilder.FileBased<Object> jsonFeeder = jsonFile("finalSimulation/jsonFeeder.json").circular();

    private static ChainBuilder authenticate =
            exec(http("Post Request of Authentication")
                    .post("/authenticate")
                    .body(ElFileBody("finalSimulation/login.json")).asJson()
                    .check(status().is(200))
                    .check(jmesPath("token").saveAs("authToken")));

    private static ChainBuilder getAllVideGames =
            exec(http("Get all games")
                    .get("/videogame")
                    .header("Authorization","Bearer #{authToken}")
                    .check(status().is(200))
                    .check(bodyString().saveAs("responseBody")))
                    .exec(session -> {
                                System.out.println("Response Body " +session.getString("responseBody"));
                                return session;
                            }
                    );
    private static ChainBuilder createGame =
            exec(http("Create a New game")
                    .post("/videogame")
                    .header("Authorization","Bearer #{authToken}")
                    .body(ElFileBody("finalSimulation/createGame.json")).asJson()
                    .check(status().is(200)));

    private static ChainBuilder getSpecficVideGame =
            feed(jsonFeeder)
                    .exec(http("Get video game with name - #{name}")
                    .get("/videogame/#{id}")
                    .header("Authorization","Bearer #{authToken}")
                    .check(status().is(200))
                            .check(jmesPath("name").isEL("#{name}")));

    private static ChainBuilder deleteLastVideGame =
            exec(http("Delete a specific game -  #{name}")
                    .delete("/videogame/#{id}")
                    .header("Authorization","Bearer #{authToken}")
                    .check(status().is(200))
                    .check(bodyString().is("Video game deleted"))
            );

    // 2 Scenario Definition
    private ScenarioBuilder scn = scenario("Final Simulation")
            .exec(authenticate)
            .pause(5)
            .exec(getAllVideGames)
            .pause(5)
            .exec(createGame)
            .pause(5)
            .exec(getSpecficVideGame)
            .pause(5)
            .exec(deleteLastVideGame);

    // 3 Load Simulation
    {
        setUp(
                scn.injectOpen(
                        rampUsers(USER_COUNT).during(RAMP_DURATION)
                )
        ).protocols(httpProtocol);
    }

    @Override
    public void after(){
        System.out.printf("The Test is finished%n");
    }
}
