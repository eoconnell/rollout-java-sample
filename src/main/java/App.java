
import static spark.Spark.*;

import com.evanoconnell.rollout.*;
import com.evanoconnell.rollout.storage.*;

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

  public static void main(String[] args) {

    IRolloutStorage storage = new MapStorage();
    final Rollout<User> rollout = new Rollout<>(storage);

    port(Integer.valueOf(System.getenv("PORT")));

    get("/feature/activate", (request, response) -> {
      rollout.activatePercentage("feature", 100);
      return "activated feature for all ";
    });

    get("/feature/deactivate", (request, response) -> {
      rollout.activatePercentage("feature", 0);
      return "deactivated feature for all ";
    });

    get("/feature/:user", (request, response) -> {
      long id = Long.parseLong(request.params(":user"), 10);
      boolean isActive = rollout.isActive("feature", new User(id));
      return "User: " + request.params(":user") + "\nActive? " + isActive;
    });
  }

}
