package edu.brown.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

import edu.brown.hashtaggolf.Game;
import edu.brown.hashtaggolf.Player;
import freemarker.template.Configuration;

/**
 * Runs the GUI for hashtag golf.
 */
public final class SparkServerWithMultiplayer {
  private static final int PORT = 1234; // change this
  private static final Gson GSON = new Gson();
  private static Map<String, Game> rooms;
  private static Map<Game, List<String>> ipAddresses;
  private static String color = "white";
  private static int levelnum = 0;
  private static int holex = 0;
  private static int holey = 0;
  private static int startx = 0;
  private static int starty = 0;
  private static int par = 0;
  private static String guihole = "gui_hole1.png";

  /**
   * Starts running the GUI for #golf
   */
  public static void run() {
    Spark.setPort(PORT);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();
    rooms = new ConcurrentHashMap<>();
    ipAddresses = new ConcurrentHashMap<>();

    // Pages
    Spark.get("/", new FrontPageHandler(), freeMarker);
    Spark.get("/start", new StartHandler(), freeMarker);
    Spark.get("/play", new PlayHandler(), new FreeMarkerEngine());
    Spark.get("/instructions", new InstructionsHandler(), new FreeMarkerEngine());
    Spark.get("/tutorial", new TutorialHandler(), new FreeMarkerEngine());
    Spark.get("/single_player_select", new SinglePlayerSelectHandler(),
        new FreeMarkerEngine());
    Spark.get("/level_select", new LevelHandler(), new FreeMarkerEngine());
    Spark.get("/multiplayer", new MultiplayerHandler(), new FreeMarkerEngine());

    Spark.get("/lobby/:room", new LobbyHandler(), new FreeMarkerEngine());
    Spark.get("/hostlobby/:room", new HostLobbyHandler(),
        new FreeMarkerEngine());


    // Front End Requesting Information

    Spark.post("/setup", new SetupHandler());
    Spark.post("/swing", new SwingHandler());
    Spark.post("/host", new HostHandler());
    Spark.post("/join", new JoinHandler());
    Spark.post("/exit", new ExitHandler());

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
   * Displays front page of #golf.
   */
  private static class InstructionsHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "instructions.ftl");
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
   * Displays tutorial page of #golf.
   */
  private static class TutorialHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "tutorial.ftl");
    }
  }

  /**
   * Displays levelselect page of #golf.
   */
  private static class LevelHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String newlevel = qm.value("level");

      if (newlevel != null){
        levelnum = Integer.parseInt(newlevel);
        
        try {
          BufferedReader reader =
              new BufferedReader(new FileReader(new File("src/main/resources/levelconfig.txt")));
          String read = "";

          for (int i = 0; i < levelnum; i++) {
            read = reader.readLine();
          }

          String[] readarr = read.split(",");
          String roomName = req.cookie("room");
          Game game = rooms.get(roomName);

          game.setLevel(readarr[0], readarr[1]);
          startx = Integer.parseInt(readarr[2]);
          starty = Integer.parseInt(readarr[3]);
          holex = Integer.parseInt(readarr[4]);
          holey = Integer.parseInt(readarr[5]);
          par = Integer.parseInt(readarr[6]);
          guihole = readarr[7];
          System.out.println(guihole);
          reader.close();
        } catch (IOException e1) {
          System.out.println("failed");
        }
      }
      
      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "level_select.ftl");
    }
  }

  /**
   * Displays player select page of #golf.
   */
  private static class SinglePlayerSelectHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      res.cookie("id", "0");

      try {
        Game game = new Game("new_hole1.png", "key.png");
        game.addPlayer("Tiger");

        int hashKey = game.hashCode();
        while (rooms.containsKey(hashKey)) {
          hashKey++;
        }
        String roomName = String.valueOf(hashKey);
        rooms.put(roomName, game);
        res.cookie("room", String.valueOf(hashKey));

      } catch (IOException e) {
        System.out.println("ERROR: Issue with loading level.");
      }

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

  private static class SetupHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      String id = req.cookie("id");
      String room = req.cookie("room");

      assert rooms.get(room) != null;
      List<Player> players = rooms.get(room).getPlayers();

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "#golf")
          .put("color", color)
          .put("players", players) 
          .put("id", id)
          .put("holex", holex)
          .put("holey", holey)
          .put("startx", startx)
          .put("starty", starty)
          .put("par", par)
          .put("guihole",guihole)
          .build();
      
      return GSON.toJson(variables);
    }
  }

  private static class ExitHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      System.out.println("Exit handler");
      int id = Integer.parseInt(req.cookie("id"));
      String room = req.cookie("room");

      Game game = rooms.get(room);
      assert game != null;

      List<Player> players = game.getPlayers();
      players.set(id, null);
      game.decrementNumPlayers();

      Map<String, Object> variables = ImmutableMap.of("title", "#golf",
          "color", color, "players", players,
          "id", id);
      return GSON.toJson(variables);
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
      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "lobby.ftl");
    }
  }

  private static class HostLobbyHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
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
      try {
        QueryParamsMap qm = req.queryMap();
        double angle = Double.parseDouble(qm.value("angle"));
        int id = Integer.parseInt(req.cookie("id"));
        String word = qm.value("word");

        String room = req.cookie("room");
        Game game = rooms.get(room);
        List<Player> players = game.swing(id, word, angle);

        game.checkResetState();

        final Map<String, Object> variables =
            new ImmutableMap.Builder<String, Object>()
            .put("players", players)
            .put("entireGameOver", game.isGameOver())
            .build();

        return GSON.toJson(variables);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }
  }

  private static class HostHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String roomName = qm.value("room");
      String playerName = qm.value("player");
      boolean success;

      try {
        success = !rooms.containsKey(roomName);
        if (success) {
          Game game = new Game("new_hole1.png", "key.png");
          List<String> ipAddressList = new ArrayList<String>();
          ipAddressList.add(req.ip());
          ipAddresses.put(game, ipAddressList);
          game.addPlayer(playerName);
          rooms.put(roomName, game);

          res.cookie("id", String.valueOf(0));
          res.cookie("room", roomName);
        }
      } catch (IOException e) {
        success = false;
        System.out.println("ERROR: Issue loading level.");
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
      boolean duplicateIp = false;

      if (roomExists) {
        Game game = rooms.get(roomName);
        List<String> ipAddressList = ipAddresses.get(game);
        
        if (ipAddressList.contains(req.ip())) {
          duplicateIp = true;
        } else {
          ipAddressList.add(req.ip());
          ipAddresses.put(game, ipAddressList);
          
          String id = game.addPlayer(playerName);
          
          if (id != null) {
            res.cookie("id", id);
            res.cookie("room", roomName);
          } else {
            roomFull = true;
          }
        }
      }

      final Map<String, Object> variables =
          new ImmutableMap.Builder<String, Object>()
          .put("roomExists", roomExists)
          .put("roomFull", roomFull)
          .put("duplicateIp", duplicateIp)
          .build();

      return GSON.toJson(variables);
    }
  }

  private static class HostStartHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      String id = req.cookie("id");
      String room = req.cookie("room");

      assert rooms.get(room) != null;
      Game game = rooms.get(room);

      boolean allOtherPlayersReady = false;
      List<Player> unreadyPlayers = game.hostStart(id);
      if (unreadyPlayers.isEmpty()) {
        allOtherPlayersReady = true;
      }

      game.checkResetState();
      final Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("startGame", allOtherPlayersReady)
          .put("unreadyPlayers", unreadyPlayers).build();
      return GSON.toJson(variables);
    }
  }

  private static class PlayerReadyHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      String id = req.cookie("id");
      String room = req.cookie("room");

      Game game = rooms.get(room);
      game.playerReady(id);
      final Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("success", true).build();
      game.checkResetState();
      return GSON.toJson(variables);
    }
  }
}
