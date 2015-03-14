package edu.brown.imageprocessing;

import edu.brown.hashtaggolf.Terrain;

public interface PixelColour {
  public String getColourAt(int x, int y);
  public Terrain getTerrainAt(int x, int y);
}
