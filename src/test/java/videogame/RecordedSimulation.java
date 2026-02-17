package videogame;

import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class RecordedSimulation extends Simulation {

  private HttpProtocolBuilder httpProtocol = http
    .baseUrl("https://www.videogamedb.uk")
    .inferHtmlResources()
    .acceptHeader("application/json")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.9")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36");
  
  private Map<CharSequence, String> headers_0 = Map.ofEntries(
    Map.entry("priority", "u=1, i"),
    Map.entry("sec-ch-ua", "Brave\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "Windows"),
    Map.entry("sec-fetch-dest", "empty"),
    Map.entry("sec-fetch-mode", "cors"),
    Map.entry("sec-fetch-site", "same-origin"),
    Map.entry("sec-gpc", "1")
  );
  
  private Map<CharSequence, String> headers_1 = Map.ofEntries(
    Map.entry("content-type", "application/json"),
    Map.entry("origin", "https://www.videogamedb.uk"),
    Map.entry("priority", "u=1, i"),
    Map.entry("sec-ch-ua", "Brave\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "Windows"),
    Map.entry("sec-fetch-dest", "empty"),
    Map.entry("sec-fetch-mode", "cors"),
    Map.entry("sec-fetch-site", "same-origin"),
    Map.entry("sec-gpc", "1")
  );


  private ScenarioBuilder scn = scenario("RecordedSimulation")
    .exec(
      http("request_0")
        .get("/api/videogame/3")
        .headers(headers_0)
    )
    .pause(4)
    .exec(
      http("request_1")
        .post("/api/videogame")
        .headers(headers_1)
        .body(RawFileBody("videogame/recordedsimulation/0001_request.json"))
    )
    .pause(8)
    .exec(
      http("request_2")
        .post("/api/authenticate")
        .headers(headers_1)
        .body(RawFileBody("videogame/recordedsimulation/0002_request.json"))
    )
    .pause(12)
    .exec(
      http("request_3")
        .post("/api/videogame")
        .headers(headers_1)
        .body(RawFileBody("videogame/recordedsimulation/0003_request.json"))
    );

  {
	  setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
  }
}
