package edu.brown.hashtaggolf;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class PlayerTest {

  @Test
  public void test() throws IOException {
    Player player = new PlayerType1("name");
    
    int before = player.getDistanceToHole();
    player.moveBall(10, 0);
    int after = player.getDistanceToHole();
    assertTrue(Math.abs(before-10) == after);
    //assertTrue(player.toString().equals());
    System.out.println(player.toString());
    /*assertTrue(player.toString().equals(
        "Player Info:\nName: name, Stroke #: 2, Distance to Hole: 178"));*/
  }

}
