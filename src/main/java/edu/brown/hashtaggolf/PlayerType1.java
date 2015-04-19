package edu.brown.hashtaggolf;

import java.io.Serializable;

/**
 * Type 1 of the special players.
 */
public class PlayerType1 extends Player implements Serializable {
  /**
   * UID
   */
  private static final long serialVersionUID = -3323328741501318106L;

  public PlayerType1(String name) {
    super(name);
  }

  public PlayerType1(String name, int startx, int starty, int holex, int holey) {
    super(name, startx, starty, holex, holey);
  }
}
