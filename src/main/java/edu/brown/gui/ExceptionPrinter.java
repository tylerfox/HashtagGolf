package edu.brown.gui;

import java.io.PrintWriter;
import java.io.StringWriter;

import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

/**
 * Exception Printer to take care of exceptions.
 */
class ExceptionPrinter implements ExceptionHandler {
  private static final int REQ_STATUS = 500;

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