package edu.brown.hashtaggolf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import edu.brown.gui.SparkServer;

/**
 * The main golf class that is used to invoke the program.
 */
public class Main {
  private boolean isGameOver = false;

  /**
   * standard main method.
   * @param args command line args
   */
  public static void main(String[] args) {
    new Main().run(args);
  }

  public Main() {
    // prints out the IP address of the host to make hosting games easier
    try {
      System.out.println(InetAddress.getLocalHost() + ":" + SparkServer.PORT);
    } catch (UnknownHostException e) {
      System.out.println("ERROR: Could not trace IP.");
    }
  }

  /**
   * Runs the program.
   */
  public void run(String[] args) {

    OptionParser parser = new OptionParser();
    parser.accepts("requireUniqueIp");
    parser.accepts("repl");

    OptionSet ops;
    try {
      ops = parser.parse(args);
    } catch (OptionException e) {
      System.out.println("ERROR: Please use ./run [--requireUniqueIp] [--repl]");
      return;
    }
    if (ops.has("repl")) {
      repl();
    } else {
      SparkServer.run(ops.has("requireUniqueIp"));
    }
  }

  /**
   * Opens a REPL to play a Command Line version of HashtagGolf.
   */
  public void repl() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        System.in))) {
      Referee ref = new Referee("new_hole1.png", "key.png");
      ref.setRepl(true);
      int scaleFactor = 1;
      ref.setScaleFactor(scaleFactor);

      System.out.println("Please enter your name.");
      String input = reader.readLine();

      if (input == null || input.equals("")) {
        input = "Tiger";
      }

      Player player = new Player(input, "", 310, 355, 971, 350, ref.getScaleFactor());
      player.setRepl(true);

      System.out.println("Hello " + input + "!  Let's play #golf.\n");
      System.out.println(player);
      System.out.println("Enter your query to swing!");
      input = reader.readLine();

      boolean prompt = true;
      int angle = 0;
      System.out.println("Enter the angle at which to swing.");

      while (prompt) {
        try {
          angle = Integer.parseInt(reader.readLine());
          prompt = false;
        } catch (NumberFormatException e) {
          System.out.println("Please enter an integer for the swing angle.");
        }
      }

      while (!isGameOver && input != null) {
        ref.swing(player, input, angle);
        if (player.isGameOver()) {
          isGameOver = true;
          System.out.println("Congrats on finishing the course.");
          System.out.println(ref);
        } else {
          System.out.println(player);
          System.out.println("Enter your query to swing!");
          input = reader.readLine();

          prompt = true;
          System.out.println("Enter the angle at which to swing.");

          while (prompt) {
            try {
              angle = Integer.parseInt(reader.readLine());
              prompt = false;
            } catch (NumberFormatException e) {
              System.out
                  .println("Please enter an integer for the swing angle.");
            }
          }
        }
      }
    } catch (IOException e) {
      System.err.print("ERROR: Issues with buffered reader in play.");
    }
  }
}
