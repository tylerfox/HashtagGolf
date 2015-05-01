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
import edu.brown.hashtaggolf.Referee;
import edu.brown.hashtaggolf.Terrain;
import freemarker.template.Configuration;

/**
 * Runs the GUI for hashtag golf.
 */
public final class SparkServer {
  private static final int PORT = 4567;
  private static final Gson GSON = new Gson();
  private static Player myPlayer;
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
    Spark.get("/player_select", new PlayerSelectHandler(),
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
  private static class PlayerSelectHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
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
      try {
        ref = new Referee("new_hole1.png", "key.png");
      } catch (IOException e) {
        System.out.println("ERROR: Files could not be opened.");
      }
      myPlayer = new Player("Brandon", "");
      Map<String, Object> variables = ImmutableMap.of("title", "#golf", "color", color);
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
      // System.err.println(angle);
      String word = qm.value("word");
      boolean outofbounds = false;
      int oldX = myPlayer.getX();
      int oldY = myPlayer.getY();
      int count = ref.swing(myPlayer, word, angle);
      if (count == -4) {
        outofbounds = true;
      }
      final Player tempPlayer = new Player(myPlayer);
      final Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("myPlayer", tempPlayer).put("outOfBounds", outofbounds)
          .put("gameOver", tempPlayer.isGameOver()).build();
      if (myPlayer.getTerrain() == Terrain.WATER) {
        myPlayer.setX(oldX);
        myPlayer.setY(oldY);
      }
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
        playerList.add(new Player(playerName, ""));
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
        List<Player> game = rooms.get(roomName);

        if (game.size() < MAX_PLAYERS) {
          res.cookie("id", String.valueOf(game.size()));
          res.cookie("room", roomName);
          game.add(new Player(playerName, ""));
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

      assert rooms.get(room) != null;
      List<Player> players = rooms.get(room);
      Player thisPlayer = players.get(Integer.parseInt(id));
      thisPlayer.setReady(true);

      boolean allPlayersReady = false;

      while (!allPlayersReady) {
        allPlayersReady = true;
        for (Player player : players) {
          allPlayersReady = allPlayersReady && player.isReady();
        }
      }

      final Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("success", true).build();
      return GSON.toJson(variables);
    }
  }
}
