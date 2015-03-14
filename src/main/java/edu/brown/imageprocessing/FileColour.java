package edu.brown.imageprocessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FileColour implements PixelColour {
  private File file;
  private BufferedImage image;

  public FileColour(String filePath) throws IOException {
    file = new File(filePath);
    image = ImageIO.read(file);
  }

  @Override
  public String getColourAt(int x, int y) {
    int clr = image.getRGB(x, y);
    int red = (clr & 0x00ff0000) >> 16;
    int green = (clr & 0x0000ff00) >> 8;
    int blue = clr & 0x000000ff;

    return "Red: " + red + ", Green: " + green + ", Blue: " + blue;
  }

  @Override
  public int getTerrainAt(int x, int y) {
    return image.getRGB(x, y);
  }
}
