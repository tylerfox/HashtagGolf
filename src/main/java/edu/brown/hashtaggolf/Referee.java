package edu.brown.hashtaggolf;

import java.io.IOException;

import twitter4j.TwitterException;
import edu.brown.imageprocessing.FileColor;
import edu.brown.imageprocessing.PixelColor;
import edu.brown.socialdata.SocialQuery;
import edu.brown.socialdata.TwitterQuery;

/**
 * Referee Class for HashtagGolf.
 */
public class Referee {
  private double scaleFactor;
  private PixelColor image;
  private int level;
  private int par;
  private SocialQuery tq;
  public static final int OUT = -4;
  private boolean repl = false;

  /**
   * Instantiates a Referee class; A referee class is created at the start of a
   * new level.
   * @param courseImage the file path for the current level
   * @throws IOException If TwitterQuery fails
   */
  public Referee(String courseImage, String terrainKey) throws IOException {
    tq = new TwitterQuery();
    this.image = new FileColor(courseImage);
    Terrain.setColors(new FileColor(terrainKey));
    this.par = 3;
  }



  /**
   * Updates all of the relevant information after a swing.
   * - gets word count
   * - gets terrain of ball
   * - checks powerUp of player for new distance
   * - if new distance is out of bounds, send out of bounds
   *  else moves ball the distance
   * @param player the player who is swinging
   * @param word the word the player input
   * @param angle the angle at which the ball should go in the direction of
   */
  public int swing(Player player, String word, double angle) {
    player.setOutOfBounds(false);
    int yards;
    String[] wordSplit = word.split(" ");
    if (wordSplit.length == 2 && wordSplit[wordSplit.length - 1].equals("-n")) {
      try {
        yards = Integer.parseInt(wordSplit[0]);
      } catch (NumberFormatException e) {
        yards = 0;
        System.err.println("Bad cheat code!");

      }
    } else {
      yards = applyEnvironment(player, word);
    }

    if (yards == -1) {
      System.err.println("Network Error. Please swing again when you have a connection.");
      return -1;
    } else if (yards == -2) {
      System.err.println("Invalid query. Please try again");
      return -2;
    } else if (yards == -3) {
      System.err.println("Problem fetching tweets. Please try again");
      return -3;
    }

    int newX = player.getX()
        + (int) (Math.cos(Math.toRadians(angle)) * yards * scaleFactor);
    int newY = player.getY()
        - (int) (Math.sin(Math.toRadians(angle)) * yards * scaleFactor);

    if (repl) {
      System.out.println("Ball hit to (" + newX + ", " + newY + ")");
    }

    Terrain newTerrain = image.getTerrainAt(newX, newY);

    switch (newTerrain) {
      case OUT_OF_BOUNDS:
        player.setOutOfBounds(true);
        player.applyStrokePenalty();
        player.moveBall(yards, angle);
        player.setTerrain(newTerrain);
        return OUT;
      default:
        player.setOutOfBounds(false);
        player.moveBall(yards, angle);
        player.setTerrain(newTerrain);
    }

    if (repl) {
      System.out.println(newTerrain);
      System.out.println("You are now at (" + player.getX() + ", " + player.getY() + ")");
    }

    player.isGameOver();
    return yards;
  }

  /**
   * Calculates the distance of the ball based on the current terain.
   * @param player - the current player
   * @param word - the query
   * @return the distance, or -1 for network error, -2 for invalid query, or -3
   * for twitter error
   */
  public int applyEnvironment(Player player, String word) {
    Terrain t = player.getTerrain();
    int seconds;
    switch (t) {
      case BUNKER:
        seconds = 30;
        break;
      case ROUGH:
        seconds = 45;
        break;
      case WATER:
        seconds = 35;
        break;
      default:
        seconds = 60;
    }
    try {
      int count = tq.getCount(word, seconds);
      return player.powerup(count);
    } catch (TwitterException e) {
      e.printStackTrace();
      if (e.isCausedByNetworkIssue()) {
        return -1;
      } else if (e.getStatusCode() == 403) {
        return -2;
      } else {
        return -3;
      }
    }
  }

  @Override
  public String toString() {
    return "Level Number: " + level + " Par: " + par;
  }

  /**
   * Sets the scale factor.
   * @param scale scale factor to set
   */
  public void setScaleFactor(double scale) {
    scaleFactor = scale;
  }

  /**
   * Gets the scale factor.
   * @return scale factor
   */
  public double getScaleFactor() {
    return scaleFactor;
  }

  /**
   * Sets repl to true/false.
   * @param repl true if want to run repl else false
   */
  public void setRepl(boolean repl) {
    this.repl = repl;
  }
}
