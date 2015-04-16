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
public final class SparkServer {
  private static final int PORT = 4567;
  private static final Gson GSON = new Gson();
  private static Player myPlayer;
  private static final int MAX_PLAYERS = 4;
  private static Referee ref;
  private static Map<String, List<Player>> rooms;

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
    Spark.get("/player_select", new PlayerSelectHandler(), new FreeMarkerEngine());
    Spark.get("/level_select", new TempHandler(), new FreeMarkerEngine());
    Spark.get("/multiplayer", new MultiplayerHandler(), new FreeMarkerEngine());

    Spark.get("/lobby/:lobby", new LobbyHandler(), new FreeMarkerEngine());

    Spark.get("/settings", new TempHandler(), new FreeMarkerEngine());

    // Front End Requesting Information

    Spark.post("/playgame", new PlayGameHandler());
    Spark.get("/host", new SetupServer(), new FreeMarkerEngine());
    Spark.post("/join", new TempHandler(), new FreeMarkerEngine());
    
    Spark.post("/swing", new SwingHandler());
    Spark.post("/host", new HostHandler());
    Spark.post("/join", new JoinHandler());
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
      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "play.ftl");
    }
  }

  private static class PlayGameHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      int startx = Integer.parseInt(qm.value("startx"));
      int starty = Integer.parseInt(qm.value("starty"));
      int holex = Integer.parseInt(qm.value("holex"));
      int holey = Integer.parseInt(qm.value("holey"));
      try {
        ref = new Referee("new_hole1.png", "key.png");
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      myPlayer = new PlayerType1("Brandon", startx, starty, holex, holey);
      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
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


      Map<String, Object> variables =
          ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "lobby.ftl");
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
      int count = ref.swing(myPlayer, word, angle);
      System.out.println("me this far! " + count);
      if (count == -4) {
        outofbounds = true;
      }
      return GSON.toJson(new Object[]{myPlayer, outofbounds});
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
        playerList.add(new PlayerType1(playerName));
      }

      final Map<String, Object> variables = new ImmutableMap
          .Builder<String, Object>()
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
          game.add(new PlayerType1(playerName));
        } else {
          roomFull = true;
        }
      }

      final Map<String, Object> variables = new ImmutableMap
          .Builder<String, Object>()
          .put("roomExists", roomExists)
          .put("roomFull", roomFull)
          .build();

      return GSON.toJson(variables);
    }
  }
}
