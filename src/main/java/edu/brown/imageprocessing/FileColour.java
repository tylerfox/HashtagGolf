package edu.brown.imageprocessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.brown.hashtaggolf.Terrain;

public class FileColour implements PixelColour {
  private File file;
  private BufferedImage image;

  public FileColour(String filePath) throws IOException {
    file = new File(filePath);
    image = ImageIO.read(file);
  }
  
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
    int colour = image.getRGB(x, y);
    
    for (Terrain terrain : Terrain.values()) {
      if (terrain.getColour() == colour) {
        return terrain;
      }
    }
    
    return null;
  }
}
