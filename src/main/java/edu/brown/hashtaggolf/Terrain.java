package edu.brown.hashtaggolf;

import java.io.IOException;

import edu.brown.imageprocessing.FileColour;
import edu.brown.imageprocessing.PixelColour;

/**
 * An enum of different terrains based on their color.
 */
public enum Terrain {
  BUNKER(-1055568),
  FAIRWAY(-14503604),
  ROUGH(-4856291),
  GREEN(-16711936),
  OUT_OF_BOUNDS(-8421505),
  WATER(-16735512),
  TEE(-1),
  HOLE(-16777216),
  WIGGLE_ROOM(-16712192);
  
  /**
  BUNKER("0"),
  FAIRWAY("1"),
  ROUGH("2"),
  GREEN("3"),
  OUT_OF_BOUNDS("4"),
  WATER("5"),
  TEE("6"),
  HOLE("7"),
  WIGGLE_ROOM("8"); */
  
  private int colour;
  private int[] xCoordinates = {50, 150, 250, 350, 450, 550, 650, 750, 850};
  private PixelColour terrainKey;

  private Terrain(String type) {
    try {
      terrainKey = new FileColour("path to terrain key");
      this.colour = terrainKey.getRGBAt(xCoordinates[Integer.parseInt(type)], 50);
    } catch (IOException e) {
      System.out.println("Terrain key file not found.");
    }
  }
    
  /**
   * Instantiates a new terrain.
   */
  private Terrain() {
    //To make code compile until we decide upon the colours for each kind of terrain.
  }

  /**
   * Instantiate a new terrain with the given color value.
   * @param colour the color of the terrain
   */
  private Terrain(int colour) {
    this.colour = colour;
  }

  /**
   * Gets the color of the terrain.
   * @return an integer representing the color of the terrain
   */
  public int getColour() {
    return colour;
  }

  @Override
  public String toString() {
    String str = "";
    switch (this) {
      case BUNKER:
        str = "Ball is in the bunker!";
        break;
      case FAIRWAY:
        str = "Ball is in the fairway!";
        break;
      case ROUGH:
        str = "Ball is in the rough!";
        break;
      case GREEN:
        str = "Ball is in the bunker!";
        break;
      case HOLE:
        str = "Congrats!  Ball made it to the hole.";
        break;
      case WATER:
        str = "Oh no!  Ball is in the water.";
        break;
      case OUT_OF_BOUNDS:
        str = "Oh no!  Ball went out of bounds.";
        break;
      case TEE:
        str = "Ball is at the Tee.";
        break;
      default:
        str = "Not sure where your ball is.";
    }

    return str;
  }
}