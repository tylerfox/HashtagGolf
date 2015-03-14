import static org.junit.Assert.*;

import java.awt.AWTException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.imageprocessing.ScreenColour;

public class ScreenColorTest {

  @BeforeClass
  public static void setUpClass() throws Exception {
    // (Optional) Code to run before any tests begin goes here.
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    // (Optional) Code to run after all tests finish goes here.
  }

  @Before
  public void setUp() {
    // (Optional) Code to run before each test case goes here.
  }

  @After
  public void tearDown() {
    // (Optional) Code to run after each test case goes here.
  }

  @Test
  public void eclipseWindow() {
    try {
      ScreenColour screen = new ScreenColour();
      
      assertTrue(screen.getColourAt(1595, 895).equals("Red: 31, Green: 42, Blue: 48"));
      assertTrue(screen.getColourAt(100, 500).equals("Red: 255, Green: 255, Blue: 255"));
      assertTrue(screen.getColourAt(1000, 60).equals("Red: 180, Green: 205, Blue: 230"));
      assertTrue(screen.getColourAt(230, 60).equals("Red: 65, Green: 98, Blue: 90"));
    } catch (AWTException e) {
      assertTrue(false);
    }
  }
  
  @Test
  public void offScreen() {
    try {
      ScreenColour screen = new ScreenColour();
      assertTrue(screen.getColourAt(200000, -387).equals("No screen of this size exists."));
    } catch (AssertionError e) {
      assertTrue(true);
    } catch (AWTException e) {
      assertTrue(false);
    }
  }
}
