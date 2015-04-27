package edu.brown.hashtaggolf;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class RefereeTest {

  @Test
  public void test() throws IOException {
    Referee ref = new Referee("new_hole1.png", "key.png");
    Player player = new PlayerType1("name", "");
    assertTrue(ref.applyEnvironment(player, "word") >= 0);

    int before = player.getDistanceToHole();
    ref.swing(player, "falksjflkaewjf", 0);
    int after = player.getDistanceToHole();
    System.out.println(player.getDistanceToHole() + " " + before + " " + after);
  }

}
