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
}