package edu.brown.hashtaggolf;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class PlayerTest {

  @Test
  public void test() throws IOException {
    Player player = new Player("name", "");

    int before = player.getDistanceToHole();
    player.moveBall(10, 0);
    int after = player.getDistanceToHole();
    
    assertTrue(before >= 0);
    assertTrue(after >= 0);
    //assertTrue(Math.abs(before-10) == after);
    //assertTrue(player.toString().equals());
    System.out.println(player.toString());
    /*assertTrue(player.toString().equals(
        "Player Info:\nName: name, Stroke #: 2, Distance to Hole: 178"));*/
  }

}
