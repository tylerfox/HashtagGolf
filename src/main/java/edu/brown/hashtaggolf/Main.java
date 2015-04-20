package edu.brown.hashtaggolf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
    new Main().run();
  }

  public Main() {
  }

  /**
   * Runs the program.
   */
  public void run() {
    // runSparkServer();
    // TwitterQuery tq = new TwitterQuery();
    // 1ST ARG IS QUERY, 2ND IS DURATION IN SECONDS
    // tq.getCount("", 60);
    SparkServer.run();
    // play();
  }

  /**
   * Opens a REPL to play a Command Line version of HashtagGolf.
   */
  public void play() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        System.in))) {
      Referee ref = new Referee("new_hole1.png", "key.png");

      System.out.println("Please enter your name.");
      String input = reader.readLine();
      if (input == null || input.equals("")) {
        input = "Player 1";
      }
      Player player = new PlayerType1(input);
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

      // if (input != null && input.startsWith("#")) {
      // input = input.substring(1);
      // }

      while (!isGameOver && input != null) {
        // TODO: Add club choice (this may be an extra argument to the swing
        // method in referee)
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
