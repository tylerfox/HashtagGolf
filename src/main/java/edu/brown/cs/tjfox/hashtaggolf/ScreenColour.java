package golf;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;

public class ScreenColour implements PixelColour {
  private Robot screen;
  
  public ScreenColour() throws AWTException {
    screen = new Robot();
  }
  
  public Color getColourAt(int x, int y) {
    Color colour = screen.getPixelColor(x, y);
    return colour;
  }
  
  public void printColourAt(int x, int y) {
    System.out.print("Red: " + getColourAt(x, y).getRed());
    System.out.print(", Green: " + getColourAt(x, y).getGreen());
    System.out.println(", Blue: " + getColourAt(x, y).getBlue());
  }
}
