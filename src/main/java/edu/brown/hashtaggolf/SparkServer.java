package edu.brown.hashtaggolf;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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

import freemarker.template.Configuration;

/**
 * Runs the GUI for hashtag golf.
 */
public final class SparkServer {
  private static final int REQ_STATUS = 500;
  private static final int PORT = 4567;
  private static final Gson GSON = new Gson();

  /**
   * Starts running the GUI for #golf
   */
  public static void run() {
    Spark.setPort(PORT);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());
    
    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes

    // Page Displayals
    Spark.get("/start", new FrontPageHandler(), freeMarker);
    Spark.get("/play", new TempHandler(), new FreeMarkerEngine());
    Spark.get("/create", new TempHandler(), new FreeMarkerEngine());
    Spark.get("/player_select", new TempHandler(), new FreeMarkerEngine());
    Spark.get("/level_select", new TempHandler(), new FreeMarkerEngine());
    Spark.get("/multiplayer", new TempHandler(), new FreeMarkerEngine());
    Spark.get("/settings", new TempHandler(), new FreeMarkerEngine());

    // Front End Requesting Information
    Spark.post("/host", new TempHandler(), new FreeMarkerEngine());
    Spark.post("/join", new TempHandler(), new FreeMarkerEngine());
    Spark.post("/swing", new TempHandler(), new FreeMarkerEngine());

  }
  
  /**
   * Displays menu page of #golf.
   * @author btai
   */
  private static class FrontPageHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables =
          ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "start.ftl");
    }
  }

  /**
   * 
   * @author Beverly
   *
   */
  private static class TempHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      Map<String, Object> variables =
          ImmutableMap.of("title", "#golf");
      return new ModelAndView(variables, "temp.ftl");
    }
  }




  /**
   * Exception Printer to take care of exceptions.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(REQ_STATUS);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
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
      System.out.printf("ERROR: Unable use %s for template "
          + "loading.%n", templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }
}
