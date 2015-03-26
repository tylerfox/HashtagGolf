package edu.brown.imageprocessing;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.hashtaggolf.Terrain;

public class TerrainTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void numericalRGB() throws IOException {
    FileColour terrainKey = new FileColour("terrain_key.png");
    assertTrue(terrainKey.getRGBAt(50, 100) == -1055568);
    assertTrue(terrainKey.getRGBAt(150, 100) == -14503604);
    assertTrue(terrainKey.getRGBAt(250, 100) == -4856291);
    assertTrue(terrainKey.getRGBAt(350, 100) == -16711936);
    assertTrue(terrainKey.getRGBAt(450, 100) == -8421505);
    assertTrue(terrainKey.getRGBAt(550, 100) == -16735512);
    assertTrue(terrainKey.getRGBAt(650, 100) == -1);
    assertTrue(terrainKey.getRGBAt(750, 100) == -16777216);
    assertTrue(terrainKey.getRGBAt(850, 100) == -16712192);
  }
  
  @Test
  public void terrains() throws IOException {
    FileColour terrainKey = new FileColour("terrain_key.png");
    assertTrue(terrainKey.getTerrainAt(50, 100) == Terrain.BUNKER);
    assertTrue(terrainKey.getTerrainAt(150, 100) == Terrain.FAIRWAY);
    assertTrue(terrainKey.getTerrainAt(250, 100) == Terrain.ROUGH);
    assertTrue(terrainKey.getTerrainAt(350, 100) == Terrain.GREEN);
    assertTrue(terrainKey.getTerrainAt(450, 100) == Terrain.OUT_OF_BOUNDS);
    assertTrue(terrainKey.getTerrainAt(550, 100) == Terrain.WATER);
    assertTrue(terrainKey.getTerrainAt(650, 100) == Terrain.TEE);
    assertTrue(terrainKey.getTerrainAt(750, 100) == Terrain.HOLE);
    assertTrue(terrainKey.getTerrainAt(850, 100) == Terrain.WIGGLE_ROOM);
  }
}
