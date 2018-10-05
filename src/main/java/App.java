
import static spark.Spark.*;

import com.evanoconnell.rollout.*;
import com.evanoconnell.rollout.storage.*;

import java.lang.NumberFormatException;

public class App {

  static class User implements IRolloutUser {
    private long id;
    public User(long id) {
      this.id = id;
    }
    public long getId() {
      return id;
    }
  }

  static int integer(String integer, int defaultValue) {
    try {
      return Integer.parseInt(integer);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  public static void main(String[] args) {

    IRolloutStorage storage = new MapStorage();
    final Rollout<User> rollout = new Rollout<>(storage);

    port(Integer.valueOf(System.getenv("PORT")));

    put("/feature/:feature/activate", (request, response) -> {
      String feature = request.params(":feature").toString();
      int percent = integer(request.queryParams("percent"), 100);
      rollout.activatePercentage(feature, percent);
      return "activated feature for "+ percent +"% of users";
    });

    put("/feature/:feature/deactivate", (request, response) -> {
      String feature = request.params(":feature").toString();
      rollout.activatePercentage(feature, percent);
      return "deactivated feature for 100% of users";
    });

    get("/feature/:feature/:user", (request, response) -> {
      String feature = request.params(":feature").toString();
      long id = Long.parseLong(request.params(":user"), 10);
      boolean isActive = rollout.isActive(feature, new User(id));
      return isActive;
    });
  }

}
