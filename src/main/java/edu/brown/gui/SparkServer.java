package edu.brown.gui;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.hashtaggolf.Player;
import freemarker.template.Configuration;

/**
 * Runs the GUI for hashtag golf.
 */
public final class SparkServer {
  private static final int PORT = 4567;
  private static final Gson GSON = new Gson();
  private static Player myPlayer;
  private static List<Player> otherPlayers;

  /**
   * Starts running the GUI for #golf
   */
  public static void run() {
    Spark.setPort(PORT);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes

    // Pages
    Spark.get("/", new FrontPageHandler(), freeMarker);
    Spark.get("/start", new StartHandler(), freeMarker);
    Spark.get("/play", new TempHandler(), new FreeMarkerEngine());
    Spark.get("/create", new TempHandler(), new FreeMarkerEngine());
    Spark.get("/player_select", new TempHandler(), new FreeMarkerEngine());
    Spark.get("/level_select", new TempHandler(), new FreeMarkerEngine());
    Spark.get("/multiplayer", new TempHandler(), new FreeMarkerEngine());
    Spark.get("/settings", new TempHandler(), new FreeMarkerEngine());

    // Front End Requesting Information
    Spark.get("/host", new SetupServer(), new FreeMarkerEngine());
    Spark.post("/join", new TempHandler(), new FreeMarkerEngine());
    Spark.post("/swing", new TempHandler(), new FreeMarkerEngine());

  }

  /**
   * Displays front page of #golf.
   */
  private static class FrontPageHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "main.ftl");
    }
  }

  /**
   * Displays menu page of #golf.
   * @author btai
   */
  private static class StartHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "start.ftl");
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
}
