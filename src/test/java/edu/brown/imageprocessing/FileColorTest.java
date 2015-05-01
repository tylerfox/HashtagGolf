package edu.brown.imageprocessing;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.imageprocessing.FileColor;

public class FileColorTest {
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
  public void frenchFlag() {
    try {
      FileColor frenchFlag = new FileColor("French_flag_design.jpg");
      
      assertTrue(frenchFlag.getColorAt(10, 15).equals("Red: 0, Green: 0, Blue: 108"));
      assertTrue(frenchFlag.getColorAt(100, 50).equals("Red: 255, Green: 255, Blue: 255"));
      assertTrue(frenchFlag.getColorAt(225, 10).equals("Red: 255, Green: 0, Blue: 0"));
    } catch (IOException e) {
      assertTrue(false);
    }
  }
  
  @Test
  public void exceedImageBounds() {
    try {
      FileColor frenchFlag = new FileColor("French_flag_design.jpg");
      frenchFlag.getColorAt(270, 219);
    } catch (ArrayIndexOutOfBoundsException e) {
      assertTrue(true);
    } catch (IOException e) {
      assertTrue(false);
    }
  }
  
  @Test
  public void fileNotFound() {
    try {
      new FileColor("not a valid filepath");
    } catch (IOException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void whiteTerrain() {
    try {
      FileColor frenchFlag = new FileColor("French_flag_design.jpg");
      assertTrue(frenchFlag != null);
      //assertTrue(frenchFlag.getTerrainAt(100, 50) == Terrain.TEE);
    } catch (IOException e) {
      assertTrue(false);
    }
  }
}

