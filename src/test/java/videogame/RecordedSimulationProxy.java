package videogame;

import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class RecordedSimulationProxy extends Simulation {

  private HttpProtocolBuilder httpProtocol = http
    .baseUrl("https://videogamedb.uk")
    .inferHtmlResources()
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("PostmanRuntime/7.51.0");
  
  private Map<CharSequence, String> headers_0 = Map.ofEntries(
    Map.entry("Content-Type", "application/json"),
    Map.entry("Postman-Token", "20304598-2420-4399-88ff-777d8fcb698e")
  );
  
  private Map<CharSequence, String> headers_1 = Map.of("Postman-Token", "5d8c665d-c418-4897-8290-fb7c93eb76a3");
  
  private Map<CharSequence, String> headers_2 = Map.ofEntries(
    Map.entry("Postman-Token", "a7f1c4de-7c31-4d43-beac-db66c1a55607"),
    Map.entry("authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2ODAyODMyOCwiZXhwIjoxNzY4MDMxOTI4fQ.2H8f6zIZTyD_BLQUOHX2wZ7f--ikJoY0P7T9wE2ynUk")
  );
  
  private Map<CharSequence, String> headers_3 = Map.ofEntries(
    Map.entry("Content-Type", "application/json"),
    Map.entry("Postman-Token", "ad5face0-e8d2-444d-b26d-34a6fc621b89"),
    Map.entry("authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2ODAyODMyOCwiZXhwIjoxNzY4MDMxOTI4fQ.2H8f6zIZTyD_BLQUOHX2wZ7f--ikJoY0P7T9wE2ynUk")
  );
  
  private Map<CharSequence, String> headers_4 = Map.ofEntries(
    Map.entry("Postman-Token", "53b7b293-111c-40ba-bc36-24a2db624eba"),
    Map.entry("authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2ODAyODMyOCwiZXhwIjoxNzY4MDMxOTI4fQ.2H8f6zIZTyD_BLQUOHX2wZ7f--ikJoY0P7T9wE2ynUk")
  );


  private ScenarioBuilder scn = scenario("RecordedSimulationProxy")
    .exec(
      http("request_0")
        .post("/api/authenticate")
        .headers(headers_0)
        .body(RawFileBody("videogame_Proxy/recordedsimulationproxy/0000_request.json"))
    )
    .pause(6)
    .exec(
      http("request_1")
        .get("/api/videogame")
        .headers(headers_1)
    )
    .pause(2)
    .exec(
      http("request_2")
        .get("/api/videogame/2")
        .headers(headers_2)
    )
    .pause(2)
    .exec(
      http("request_3")
        .post("/api/videogame")
        .headers(headers_3)
        .body(RawFileBody("videogame_Proxy/recordedsimulationproxy/0003_request.json"))
    )
    .pause(5)
    .exec(
      http("request_4")
        .delete("/api/videogame/3")
        .headers(headers_4)
    );

  {
	  setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
  }
}
