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
  private static boolean uniqueIpRequired = false;

  /**
   * Starts running the GUI for #golf
   */
  public static void run(boolean requireUniqueIp) {
    Spark.setPort(PORT);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();
    rooms = new ConcurrentHashMap<>();
    ipAddresses = new ConcurrentHashMap<>();
    uniqueIpRequired = requireUniqueIp;

    // Pages
    Spark.get("/", new FrontPageHandler(), freeMarker);
    Spark.get("/start", new StartHandler(), freeMarker);
    Spark.get("/play", new PlayHandler(), new FreeMarkerEngine());
    Spark.get("/instructions", new InstructionsHandler(), new FreeMarkerEngine());
    Spark.get("/tutorial", new TutorialHandler(), new FreeMarkerEngine());
    Spark.get("/single_player_select", new SinglePlayerSelectHandler(),
        new FreeMarkerEngine());
    Spark.get("/level_select", new LevelHandler(), new FreeMarkerEngine());
    Spark.get("/multi_levelselect", new MultiLevelHandler(), new FreeMarkerEngine());
    Spark.get("/multiplayer", new MultiplayerHandler(), new FreeMarkerEngine());

    Spark.get("/lobby/:room", new LobbyHandler(), new FreeMarkerEngine());
    Spark.get("/hostlobby", new HostLobbyHandler(),
        new FreeMarkerEngine());


    // Front End Requesting Information
    Spark.post("/setup", new SetupHandler());
    Spark.post("/exit", new ExitHandler());
    Spark.post("/swing", new SwingHandler());
    Spark.post("/spectate", new SpectateHandler());

    // Hosting and joining post requests
    Spark.post("/host", new HostHandler());
    Spark.post("/join", new JoinHandler());
    Spark.post("/hoststart", new HostStartHandler());
    Spark.post("/ready", new PlayerReadyHandler());
    Spark.post("/joinedPlayers", new JoinedPlayersHandler());
    Spark.post("/availableRooms", new AvailableRoomsHandler());
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
   * Displays single level select page of #golf.
   */
  private static class LevelHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      levelSelect(req, res);
      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "level_select.ftl");
    }
  }

  /**
   * Displays multiplayer level select page of #golf.
   */
  private static class MultiLevelHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      levelSelect(req, res);
      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "multi_level_select.ftl");
    }
  }

  private static void levelSelect(Request req, Response res) {
    QueryParamsMap qm = req.queryMap();
    String newlevel = qm.value("level");

    if (newlevel != null){
      int levelnum = Integer.parseInt(newlevel);

      try {
        BufferedReader reader = new BufferedReader(new FileReader(
            new File("src/main/resources/levelconfig.txt")));
        String read = "";

        for (int i = 0; i < levelnum; i++) {
          read = reader.readLine();
        }

        String[] readarr = read.split(",");
        String roomName = req.cookie("room");
        Game game = rooms.get(roomName);

        int startx = Integer.parseInt(readarr[2]);
        int starty = Integer.parseInt(readarr[3]);
        int holex = Integer.parseInt(readarr[4]);
        int holey = Integer.parseInt(readarr[5]);
        int par = Integer.parseInt(readarr[6]);
        String guihole = readarr[7];
        game.setLevel(readarr[0], readarr[1], startx,
            starty, holex, holey, par, guihole);
        reader.close();

      } catch (IOException e1) {
        System.out.println("ERROR: Level reading failed");
      }
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
        game.setActive(true);

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
      Game game = rooms.get(room);
      List<Player> players = game.getPlayers();

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "#golf")
          .put("color", color)
          .put("players", players)
          .put("id", id)
          .put("holex", game.getHoleX())
          .put("holey", game.getHoleY())
          .put("startx", game.getStartX())
          .put("starty", game.getStartY())
          .put("par", game.getPar())
          .put("guihole", game.getGuihole())
          .build();

      return GSON.toJson(variables);
    }
  }

  private static class ExitHandler implements Route {
    @Override
    public Object handle(Request req, Response res) {
      int id = Integer.parseInt(req.cookie("id"));
      String room = req.cookie("room");

      Game game = rooms.get(room);
      assert game != null;
      List<Player> players = game.getPlayers();

      players.set(id, null);
      game.decrementNumPlayers();

      // if all players left the game, then remove the room from the hashmap
      if (game.getNumPlayers() == 0) {
        rooms.remove(room);
      }

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
      String roomName = req.cookie("room");
      Map<String, Object> variables = ImmutableMap.of("title", "#golf",
          "roomName", roomName);
      return new ModelAndView(variables, "lobby.ftl");
    }
  }

  private static class HostLobbyHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String roomName = req.cookie("room");
      Map<String, Object> variables = ImmutableMap.of("title", "#golf",
          "roomName", roomName);
      return new ModelAndView(variables, "hostlobby.ftl");
    }
  }

  /**
   * Swings.
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
        boolean entireGameOver = game.isGameOver();

        if (entireGameOver) {
          rooms.remove(room);
        }
        final Map<String, Object> variables =
            new ImmutableMap.Builder<String, Object>()
            .put("players", players)
            .put("entireGameOver", entireGameOver)
            .build();

        return GSON.toJson(variables);
      } catch (Exception e) {
        e.printStackTrace(); //TODO: get rid of this
      }
      return null;
    }
  }

  /**
   * Spectates.
   */
  private static class SpectateHandler implements Route {

    @Override
    public Object handle(Request req, Response res) {
      try {
        int id = Integer.parseInt(req.cookie("id"));
        String room = req.cookie("room");
        Game game = rooms.get(room);
        List<Player> players = null;
        if (game != null) {
          players = game.spectate(id);

          game.checkResetState();
          boolean entireGameOver = game.isGameOver();

          if (entireGameOver) {
            rooms.remove(room);
          }
          final Map<String, Object> variables =
              new ImmutableMap.Builder<String, Object>()
              .put("players", players)
              .put("entireGameOver", entireGameOver)
              .build();
          return GSON.toJson(variables);
        }
      } catch (Exception e) {
        e.printStackTrace(); //TODO: get rid of this
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
        }

        String id = game.addPlayer(playerName);
        if (id != null) {
          res.cookie("id", id);
          res.cookie("room", roomName);
        } else {
          roomFull = true;
        }
      }

      final Map<String, Object> variables =
          new ImmutableMap.Builder<String, Object>()
          .put("roomExists", roomExists)
          .put("roomFull", roomFull)
          .put("duplicateIp", duplicateIp && uniqueIpRequired)
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

      final Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("startGame", allOtherPlayersReady)
          .put("unreadyPlayers", unreadyPlayers).build();
      game.checkResetState();
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

  /**
   * Returns the players in the game.
   */
  private static class JoinedPlayersHandler implements Route {

    @Override
    public Object handle(Request req, Response res) {
      String room = req.cookie("room");
      Game game = rooms.get(room);
      List<Player> players = game.getCopyOfPlayers();

      final Map<String, Object> variables =
          new ImmutableMap.Builder<String, Object>()
          .put("players", players)
          .build();
      return GSON.toJson(variables);

    }
  }

  /**
   * Returns the available rooms.
   */
  private static class AvailableRoomsHandler implements Route {

    @Override
    public Object handle(Request req, Response res) {
      List<String> listRooms = new ArrayList<>();
      for (String room : rooms.keySet()) {
        Game game = rooms.get(room);
        if (game != null && !game.isActive()
            && game.getNumPlayers() < game.getMaxPlayers()) {
          listRooms.add(room);
        }
      }

      final Map<String, Object> variables =
          new ImmutableMap.Builder<String, Object>()
          .put("rooms", listRooms)
          .build();
      return GSON.toJson(variables);

    }
  }
}
