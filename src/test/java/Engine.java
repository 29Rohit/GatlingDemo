import io.gatling.app.Gatling;
import scala.collection.mutable.Map;
import scala.jdk.javaapi.CollectionConverters;

import java.util.HashMap;

public class Engine {

  public static void main(String[] args) {

    HashMap<String, Object> props = new HashMap<>();
    props.put("gatling.core.directory.resources", "src/test/resources");
    props.put("gatling.core.directory.results", "target/gatling");
    props.put("gatling.core.directory.binaries", "target/test-classes");
    props.put("gatling.core.simulationClass", "DemoTest.VideoGame_Methods");

    Map<String, Object> scalaProps = CollectionConverters.asScala(props);

    Gatling.fromMap(scalaProps);
  }
}
