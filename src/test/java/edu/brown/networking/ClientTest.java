package edu.brown.networking;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.hashtaggolf.Player;
import edu.brown.hashtaggolf.PlayerType1;

public class ClientTest {

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
  public void handshakeTest() {
    Server server = new Server(4567);
    server.run();
    
    Client client = new Client("localhost", 4567);
    assertTrue(client.handshake("localhost", 4567));
  }
  
  @Test
  public void sendInfoTest() {
    Client client = new Client("localhost", 4567);
    Player player1 = new PlayerType1("Tiger Woods");
    assertTrue(client.sendInfo(player1));
  }
}
