package edu.brown.hashtaggolf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.brown.socialdata.TwitterQuery;


/**
 * The main stars class that is used to invoke the program.
 */
public class Main {
  private boolean isGameOver = false;
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
    //TwitterQuery tq = new TwitterQuery();
    // 1ST ARG IS QUERY, 2ND IS DURATION IN SECONDS
    //tq.getCount("#tbt", 60);
    play();
  }

  public void play() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
      Referee ref = new Referee("image file");

      System.out.println("Please enter your name.");
      String input = reader.readLine();
      Player player = new PlayerType1(input);
      System.out.println("Hello " + input + "!  Let's play #golf.\n");
      System.out.println(player);
      System.out.println("Enter your query to swing!");
      input = reader.readLine();
      
      while (!isGameOver && input != null) {
        ref.swing(player, input, 0);
        if (player.isGameOver()) {
          System.out.println("Congrats on finishing the course.");
          System.out.println(ref);
        } else {
          System.out.println(player);
          System.out.println("Enter your query to swing.");
          input = reader.readLine();
        }
      }
    } catch (IOException e) {
      System.err.print("ERROR: Issues with buffered reader in play.");
    }
  }

}
