
import static spark.Spark.*;

public class App {

  public static void main(String[] args) {

    port(Integer.valueOf(System.getenv("PORT")));

    get("/feature/:user", (request, response) -> {
      return "User: " + request.params(":user");
    });
  }

}
