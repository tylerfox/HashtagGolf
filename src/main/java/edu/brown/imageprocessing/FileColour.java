package edu.brown.imageprocessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FileColour implements PixelColour {
  File file;

  public FileColour(String filePath) {
    file = new File(filePath);
  }

  @Override
  public void printColourAt(int x, int y) {
    try {
      BufferedImage image = ImageIO.read(file);

      int clr = image.getRGB(x, y);
      int red = (clr & 0x00ff0000) >> 16;
      int green = (clr & 0x0000ff00) >> 8;
      int blue = clr & 0x000000ff;

      System.out.print("Red: " + red);
      System.out.print(", Green: " + green);
      System.out.println(", Blue: " + blue);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
