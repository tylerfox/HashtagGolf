package edu.brown.imageprocessing;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;

import edu.brown.hashtaggolf.Terrain;

/**
 * Uses the actual screen to get the color at a coordinate.
 */
public class ScreenColor implements PixelColor {
  private Robot screen;

  /**
   * Instantiate a new ScreenColor
   * @throws AWTException If an AWTException was found
   */
  public ScreenColor() throws AWTException {
    screen = new Robot();
  }

  @Override
  public String getColorAt(int x, int y) {
    Color color = screen.getPixelColor(x, y);

    int red = color.getRed();
    int green = color.getGreen();
    int blue = color.getBlue();

    return "Red: " + red + ", Green: " + green + ", Blue: " + blue;
  }

  @Override
  public Terrain getTerrainAt(int x, int y) {
    Color color = screen.getPixelColor(x, y);

    for (Terrain terrain : Terrain.values()) {
      if (terrain.getColor() == color.getRGB()) {
        return terrain;
      }
    }

    return null;
  }

  @Override
  public int getRGBAt(int x, int y) {
    Color color = screen.getPixelColor(x, y);
    return color.getRGB();
  }
}
