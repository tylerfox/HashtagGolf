package edu.brown.hashtaggolf;

import java.io.IOException;

import twitter4j.TwitterException;
import edu.brown.imageprocessing.PixelColour;
import edu.brown.socialdata.SocialQuery;
import edu.brown.socialdata.TwitterQuery;

public class Referee {
  private PixelColour image;
  private int level;
  private int par;
  private SocialQuery tq;

  public Referee(String courseImage) throws IOException {
    tq = new TwitterQuery();
    // this.image = new FileColour(courseImage);
  }

  // gets word count
  // gets terrain of ball
  // checks powerUp of player for new distance
  // if new distance is out of bounds,
  // send out of bounds,
  // else moves ball the distance

  // possibly add a club option
  public void swing(Player player, String word, double angle) {
    int yards = applyEnvironment(player, word);
    if (yards == -1) {
      System.out
          .println("Network Error. Please swing again when you have a connection.");
      return;
    } else if (yards == -2) {
      System.out.println("Invalid query. Please try again");
      return;
    } else if (yards == -3) {
      System.out.println("Problem fetching tweets. Please try again");
      return;
    }
    // TODO: Add image processing (currently is not done yet)
    // will look something like: image.getTerrainAt(player.getX() + yards, 0);
    Terrain newTerrain = Terrain.FAIRWAY;

    switch (newTerrain) {
      case OUT_OF_BOUNDS:
        player.outOfBounds();
        break;
      default:
        player.moveBall(yards, 0);
        player.setTerrain(newTerrain);
    }

    System.out.println(newTerrain);
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
      return player.powerup(tq.getCount("#" + word, seconds));
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
