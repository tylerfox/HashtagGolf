package edu.brown.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.hashtaggolf.Player;
import edu.brown.hashtaggolf.PlayerType1;
import edu.brown.hashtaggolf.Referee;
import freemarker.template.Configuration;

/**
 * Runs the GUI for hashtag golf.
 */
public final class SparkServerWithMultiplayer {
  private static final int PORT = 4567;
  private static final Gson GSON = new Gson();
  private static final int MAX_PLAYERS = 4;
  private static Referee ref;
  private static Map<String, List<Player>> rooms;
  private static boolean start = false;
  private static String color = "white";

  /**
   * Starts running the GUI for #golf
   */
  public static void run() {
    Spark.setPort(PORT);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();
    rooms = new HashMap<>();
    // Setup Spark Routes

    // Pages
    Spark.get("/", new FrontPageHandler(), freeMarker);
    Spark.get("/start", new StartHandler(), freeMarker);
    Spark.get("/play", new PlayHandler(), new FreeMarkerEngine());
    Spark.get("/create", new TempHandler(), new FreeMarkerEngine());
    Spark.get("/single_player_select", new SinglePlayerSelectHandler(),
        new FreeMarkerEngine());
    Spark.get("/level_select", new TempHandler(), new FreeMarkerEngine());
    Spark.get("/multiplayer", new MultiplayerHandler(), new FreeMarkerEngine());
    // Spark.get("/multiplay", new MultiPlayHandler(), new FreeMarkerEngine());

    Spark.get("/lobby/:room", new LobbyHandler(), new FreeMarkerEngine());
    Spark.get("/hostlobby/:room", new HostLobbyHandler(),
        new FreeMarkerEngine());

    Spark.get("/settings", new TempHandler(), new FreeMarkerEngine());

    // Front End Requesting Information
    Spark.post("/ballselect", new BallSelectHandler());
    Spark.post("/swing", new SwingHandler());
    Spark.post("/host", new HostHandler());
    Spark.post("/join", new JoinHandler());

    Spark.post("/hoststart", new HostStartHandler());
    Spark.post("/ready", new PlayerReadyHandler());
  }

  /**
   * Displays front page of #golf. Sets cookie information.
   */
  private static class FrontPageHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "main.ftl");
    }
  }

  /**
   * Displays front page of #golf.
   */
  private static class MultiplayerHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "multiplayer.ftl");
    }
  }

  /**
   * Displays menu page of #golf.
   */
  private static class StartHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "start.ftl");
    }
  }

  /**
   * Displays player select page of #golf.
   */
  private static class SinglePlayerSelectHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      res.cookie("id", "0");
      Player p = new PlayerType1("Player 1");
      List<Player> players = new ArrayList<>();
      players.add(p);

      int hashKey = p.hashCode();
      while (rooms.containsKey(hashKey)) {
        hashKey++;
      }

      rooms.put(String.valueOf(hashKey), players);
      res.cookie("room", String.valueOf(hashKey));

      Map<String, Object> variables = ImmutableMap.of("title", "#golf", "id", "0");
      return new ModelAndView(variables, "player_select.ftl");
    }
  }

  /**
   * Displays main gameplay of #golf.
   */
  private static class PlayHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String newcolor = qm.value("color");
      if(newcolor != null) {
        color = newcolor;
      }
      Map<String, Object> variables = ImmutableMap.of("title", "#golf", "color", color);
      return new ModelAndView(variables, "play2.ftl");
    }
  }

  private static class BallSelectHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
     String id = req.cookie("id");
     String room = req.cookie("room");
     List<Player> players = rooms.get(room);
     assert players != null;

      try {
        ref = new Referee("new_hole1.png", "key.png");
      } catch (IOException e) {
        System.out.println("ERROR: Files could not be opened.");
      }

      Map<String, Object> variables = ImmutableMap.of("title", "#golf", "color", color,
          "id", id);
      return GSON.toJson(variables);
    }
  }

  /**
   * Temporary Handler for unimplemented buttons.
   * @author Beverly
   */
  private static class TempHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "temp.ftl");
    }
  }

  /**
   * Uses template for FrontHandler.
   * @return FreeMarkerEngine to use for FrontHandler
   */
  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template " + "loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   * Sets cookies.
   */
  private static class LobbyHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      while (!start) {
        if (start) {
          break;
        }
      }

      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "lobby.ftl");
    }
  }

  private static class HostLobbyHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      start = true;

      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "hostlobby.ftl");
    }
  }

  /**
   * Displays menu page of #golf.
   */
  private static class SwingHandler implements Route {

    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      double angle = Double.parseDouble(qm.value("angle"));
      String word = qm.value("word");
      boolean outofbounds = false;

      String room = req.cookie("room");
      List<Player> players = rooms.get(room);
      int id = Integer.parseInt(req.cookie("id"));
      Player myPlayer = players.get(id);
      int count = ref.swing(myPlayer, word, angle);
      if (count == Referee.OUT) {
        outofbounds = true;
      }

      myPlayer.setReady(true);
      waitUntilAllPlayersReady(room);
      final Map<String, Object> variables =
          new ImmutableMap.Builder<String, Object>()
          .put("myPlayer", myPlayer)
          .put("outOfBounds", outofbounds)
          .put("gameOver", myPlayer.isGameOver())
          .put("players", players)
          .put("playerId", id)
          .build();
      return GSON.toJson(variables);
    }
  }

  private static class HostHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String roomName = qm.value("room");
      String playerName = qm.value("player");
      boolean success = !rooms.containsKey(roomName);

      if (success) {
        List<Player> playerList = new ArrayList<>();
        rooms.put(roomName, playerList);
        res.cookie("id", String.valueOf(0));
        res.cookie("room", roomName);
        playerList.add(new PlayerType1(playerName));
      }

      final Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("success", success).build();
      return GSON.toJson(variables);
    }
  }

  private static class JoinHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String roomName = qm.value("room");
      String playerName = qm.value("player");

      boolean roomExists = rooms.containsKey(roomName);
      boolean roomFull = false;

      if (roomExists) {
        List<Player> room = rooms.get(roomName);

        if (room.size() < MAX_PLAYERS) {
          res.cookie("id", String.valueOf(room.size()));
          res.cookie("room", roomName);
          room.add(new PlayerType1(playerName));
        } else {
          roomFull = true;
        }
      }

      final Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("roomExists", roomExists).put("roomFull", roomFull).build();

      return GSON.toJson(variables);
    }
  }

  private static class HostStartHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      String id = req.cookie("id");
      String room = req.cookie("room");

      assert rooms.get(room) != null;
      List<Player> players = rooms.get(room);
      Player thisPlayer = players.get(Integer.parseInt(id));
      thisPlayer.setReady(true);
      boolean allPlayersReady = true;

      // we can indicate which players are not ready if we want
      for (Player player : players) {
        allPlayersReady = allPlayersReady && player.isReady();
      }
      if (!allPlayersReady) {
        thisPlayer.setReady(false);
      }

      final Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("startGame", allPlayersReady).build();

      return GSON.toJson(variables);
    }
  }

  private static class PlayerReadyHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      String id = req.cookie("id");
      String room = req.cookie("room");

      List<Player> players = rooms.get(room);
      Player thisPlayer = players.get(Integer.parseInt(id));
      thisPlayer.setReady(true);

      waitUntilAllPlayersReady(room);

      final Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("success", true).build();
      return GSON.toJson(variables);
    }
  }

  /**
   * Holds a thread until all players in a particular room are ready.
   * @param room The room of players to wait on.
   */
  private static void waitUntilAllPlayersReady(String room) {
    List<Player> players = rooms.get(room);

    boolean allPlayersReady = false;

    while (!allPlayersReady) {
      allPlayersReady = true;

      for (Player player : players) {
        if (!player.isReady()) {
          allPlayersReady = false;
        }
      }
    }

    for (Player player : players) {
      if (player.isGameOver()) {
        player.setReady(true);
      } else {
        player.setReady(false);
      }
    }
  }
}
