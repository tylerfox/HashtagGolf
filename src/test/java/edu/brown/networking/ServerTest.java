//package edu.brown.networking;
//import static org.junit.Assert.*;
//
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import edu.brown.hashtaggolf.Player;
//import edu.brown.hashtaggolf.PlayerType2;
//
//public class ServerTest {
//
//  @BeforeClass
//  public static void setUpClass() throws Exception {
//    // (Optional) Code to run before any tests begin goes here.
//  }
//
//  @AfterClass
//  public static void tearDownClass() throws Exception {
//    // (Optional) Code to run after all tests finish goes here.
//  }
//
//  @Before
//  public void setUp() {
//    // (Optional) Code to run before each test case goes here.
//  }
//
//  @After
//  public void tearDown() {
//    // (Optional) Code to run after each test case goes here.
//  }
//
//  @Test
//  public void portTest() {
//    Server server = new Server(4567);
//    assertTrue(server.getPort() == 4567);
//  }
//  
//  @Test
//  public void handshakeTest() {
//    Server server = new Server(4567);
//    server.run();
//    assertTrue(server.handshake());
//  }
//  
//  @Test
//  public void sendStateTest() {
//    Server server = new Server(4567);
//    assertTrue(server.sendState());
//  }
//  
//  @Test
//  public void updateStateTest() {
//    Server server = new Server(4567);
//    Player player2 = new PlayerType2("Rory McIlroy");
//    assertTrue(server.updateState(1, player2));
//  }
//}
