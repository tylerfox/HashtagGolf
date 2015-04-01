package edu.brown.imageprocessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.brown.hashtaggolf.Terrain;

/**
 * Uses the file to get the color at a coordinate.
 */
public class FileColour implements PixelColour {
  private File file;
  private BufferedImage image;

  /**
   * Instantiates a new FileColour.
   * @param filePath the file path of the level
   * @throws IOException if something goes wrong while reading the file
   */
  public FileColour(String filePath) throws IOException {
    file = new File(filePath);
    image = ImageIO.read(file);
  }

  /**
   * Gets the color at the coordinate.
   * @param x the x coordinate of the color to find
   * @param y the y coordinate of the color to find
   * @return the integer representing the color at the coordinate
   */
  public int getRGBAt(int x, int y) {
    return image.getRGB(x, y);
  }

  @Override
  public String getColourAt(int x, int y) {
    int colour = image.getRGB(x, y);
    int red = (colour & 0x00ff0000) >> 16;
    int green = (colour & 0x0000ff00) >> 8;
    int blue = colour & 0x000000ff;

    return "Red: " + red + ", Green: " + green + ", Blue: " + blue;
  }

  @Override
  public Terrain getTerrainAt(int x, int y) {

    if (0 < x && x < image.getWidth() && 0 < y && y < image.getHeight()) {
      int color = image.getRGB(x, y);
      for (Terrain terrain : Terrain.values()) {
        if (terrain.getColour() == color) {
          return terrain;
        }
      }
    }
    
    return Terrain.OUT_OF_BOUNDS;
  }
}
