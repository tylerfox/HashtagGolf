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

import edu.brown.socialdata.TwitterQuery;
import freemarker.template.Configuration;

/**
 * The main stars class that is used to invoke the program.
 */
public class Main {

  private static final int ERROR_STATUS = 500;

  /**
   * standard main method.
   *
   * @param args
   *          command line args
   */
  public static void main(String[] args) {
    new Main().run();
  }

  public Main() {
  }

  public void run() {
    // runSparkServer();
    TwitterQuery.query("timbers");
  }

  /**
   * Creates spark engine.
   *
   * @return the freemarkerengine
   */
  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   * Runs spark server.
   */
  private void runSparkServer() {
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/", new FrontHandler(), freeMarker);
  }

  /**
   * Handler for the 'home' page /stars.
   *
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "#Golf").build();
      return new ModelAndView(variables, "main.ftl");
    }
  }

  /**
   * Prints exceptions to the gui.
   *
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(ERROR_STATUS);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
