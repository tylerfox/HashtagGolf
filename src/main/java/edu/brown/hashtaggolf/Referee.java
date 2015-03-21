package edu.brown.hashtaggolf;

import java.io.IOException;

import edu.brown.imageprocessing.FileColour;
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
    //this.image = new FileColour(courseImage);
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
    Terrain newTerrain = Terrain.FAIRWAY;//image.getTerrainAt(player.getX() + yards, 0);
    
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

  public int applyEnvironment(Player player, String word) {
    Terrain t = player.getTerrain();
    int yards = 0;
    
    switch (t) {
      case BUNKER:
        yards = player.powerUp(tq.getCount("#" + word, 20));
        break;
      case ROUGH:
        player.powerUp(tq.getCount("#" + word, 50));;
        break;
      case WATER:
        player.powerUp(tq.getCount("#" + word, 45));
        break;
      default:
        yards = player.powerUp(tq.getCount("#" + word, 60));
    }
    
    return yards;
    //return 50;
  }
  
  @Override
  public String toString() {
    return "Level Number: " + level + " Par: " + par;
  }
}
