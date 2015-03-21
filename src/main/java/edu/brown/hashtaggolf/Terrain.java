package edu.brown.hashtaggolf;

public enum Terrain {
  BUNKER, FAIRWAY, ROUGH, GREEN, HOLE, WATER, TEE (-1), OUT_OF_BOUNDS;

  private int colour;

  private Terrain() {
    //To make code compile until we decide upon the colours for each kind of terrain.
  }

  private Terrain(int colour) {
    this.colour = colour;
  }

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