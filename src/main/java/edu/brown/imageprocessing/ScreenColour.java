package edu.brown.imageprocessing;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;

import edu.brown.hashtaggolf.Terrain;

public class ScreenColour implements PixelColour {
  private Robot screen;

  public ScreenColour() throws AWTException {
    screen = new Robot();
  }
  
  @Override
  public String getColourAt(int x, int y) {
    Color colour = screen.getPixelColor(x, y);
    int red = colour.getRed();
    int green = colour.getGreen();
    int blue = colour.getBlue();
    
    return "Red: " + red + ", Green: " + green + ", Blue: " + blue;
  }

  @Override
  public Terrain getTerrainAt(int x, int y) {
    Color colour = screen.getPixelColor(x, y);
    
    for (Terrain terrain : Terrain.values()) {
      if (terrain.getColour() == colour.getRGB()) {
        return terrain;
      }
    }
    
    return null;
  }
}
