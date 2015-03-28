package edu.brown.imageprocessing;

import edu.brown.hashtaggolf.Terrain;

/**
 * The interface of the image processing classes.
 */
public interface PixelColour {

  /**
   * Gets the color at the coordinate.
   * @param x the x coordinate of the color to find
   * @param y the y coordinate of the color to find
   * @return the string representing the color at the coordinate
   */
  public String getColourAt(int x, int y);

  /**
   * Gets the terrain at the coordinate.
   * @param x the x coordinate of the terrain to find
   * @param y the y coordinate of the terrain to find
   * @return the terrain at the coordinate
   */
  public Terrain getTerrainAt(int x, int y);
}
