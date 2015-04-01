package edu.brown.hashtaggolf;

import edu.brown.imageprocessing.FileColor;

/**
 * An enum of different terrains based on their color.
 */
public enum Terrain {
  BUNKER(50),
  FAIRWAY(150),
  ROUGH(250),
  GREEN(350),
  OUT_OF_BOUNDS(450),
  WATER(550),
  TEE(650),
  HOLE(750);

  /**
  BUNKER(-1055568),
  FAIRWAY(-14503604),
  ROUGH(-4856291),
  GREEN(-16711936),
  OUT_OF_BOUNDS(-8421505),
  WATER(-16735512),
  TEE(-1),
  HOLE(-16777216),
  WIGGLE_ROOM(-16712192); */

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

  private int color;
  private int xCoordinate;

  /**
   * Instantiates a new terrain.
   * @param xCoordinate The x-coordinate of this kind of terrain on the terrain key.
   */
  private Terrain(int xCoordinate) {
    this.xCoordinate = xCoordinate;
  }

  public static void setColors(FileColor terrainKey) {
    for (Terrain terrain : Terrain.values()) {
      terrain.setColor(terrainKey.getRGBAt(terrain.xCoordinate, 50));
    }
  }

  /**
   * Gets the color of the terrain.
   * @return an integer representing the color of the terrain
   */
  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = color;
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
        str = "Ball is in the green!";
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

  //private int[] xCoordinates = {50, 150, 250, 350, 450, 550, 650, 750, 850};

  /**
  private Terrain(String type) {
    try {
      terrainKey = new FileColor("path to terrain key");
      this.color = terrainKey.getRGBAt(xCoordinates[Integer.parseInt(type)], 50);
    } catch (IOException e) {
      System.out.println("Terrain key file not found.");
    }
  } */
}