package edu.brown.hashtaggolf;

import edu.brown.socialdata.TwitterQuery;


/**
 * The main stars class that is used to invoke the program.
 */
public class Main {

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
    TwitterQuery tq = new TwitterQuery();
    // 1ST ARG IS QUERY, 2ND IS DURATION IN SECONDS
    tq.getCount("#DaylightSavingTime", 300);
  }

}
