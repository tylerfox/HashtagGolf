package edu.brown.hashtaggolf;

import java.io.IOException;

import twitter4j.TwitterException;
import edu.brown.imageprocessing.FileColour;
import edu.brown.imageprocessing.PixelColour;
import edu.brown.socialdata.SocialQuery;
import edu.brown.socialdata.TwitterQuery;

/**
 * Referee Class for HashtagGolf.
 *
 */
public class Referee {
  private final double SCALE_FACTOR = 3.5;
  private PixelColour image;
  private int level;
  private int par;
  private SocialQuery tq;

  /**
   * Instantiates a Referee class; A referee class is
   * created at the start of a new level.
   * @param courseImage the file path for the current level
   * @throws IOException If TwitterQuery fails
   */
  public Referee(String courseImage, String terrainKey) throws IOException {
    tq = new TwitterQuery();

    this.image = new FileColour(courseImage);
    Terrain.setColours(new FileColour(terrainKey));
    this.par = 3;

  }

  // gets word count
  // gets terrain of ball
  // checks powerUp of player for new distance
  // if new distance is out of bounds,
  // send out of bounds,
  // else moves ball the distance

  // possibly add a club option
  /**
   * Updates all of the relevant information after a swing.
   * @param player the player who is swinging
   * @param word the word the player input
   * @param angle the angle at which the ball should go in the direction of
   */
  public void swing(Player player, String word, double angle) {
    int yards = applyEnvironment(player, word);
    if (yards == -1) {
      System.out.println("Network Error. Please swing again when you have a connection.");
      return;
    } else if (yards == -2) {
      System.out.println("Invalid query. Please try again");
      return;
    } else if (yards == -3) {
      System.out.println("Problem fetching tweets. Please try again");
      return;
    }
    
    int newX = player.getX() + (int) (Math.cos(Math.toRadians(angle)) * yards * SCALE_FACTOR);
    int newY = player.getY() - (int) (Math.sin(Math.toRadians(angle)) * yards * SCALE_FACTOR);
    
    System.out.println("Ball hit to (" + newX + ", " + newY + ")");
    Terrain newTerrain = image.getTerrainAt(newX, newY);
    
    switch (newTerrain) {
      case OUT_OF_BOUNDS:
        player.outOfBounds();
        break;
      default:
        player.moveBall(yards, angle);
        player.setTerrain(newTerrain);
    }

    System.out.println(newTerrain);
    System.out.println("You are now at (" + player.getX() + ", " + player.getY() + ")");
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
        seconds = 20;
        break;
      case ROUGH:
        seconds = 50;
        break;
      case WATER:
        seconds = 45;
        break;
      default:
        seconds = 60;
    }
    try {
      return player.powerup(tq.getCount(word, seconds));
    } catch (TwitterException e) {
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
}
