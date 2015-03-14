package edu.brown.networking;

import java.net.Socket;
import java.util.List;

import edu.brown.hashtaggolf.Player;

public class Server {
  private List<Player> gameState;
  private int portNum;
  private List<Socket> clients;

  // runs server until all Players have connected
  public static void run() {
  	// waits for handshake from all Players (until Host sends start game)
  }

  // Creates socket for the client, opens input and output streams
  // Adds socket for client to the clients list
  // Sends the client id back to the client
  public static void handShake() {
  }

  // updates the state of the player with the given id
  private static void updateState(int id, Player player) {
  }

  // sends new state to all clients
  private static void sendState() {
  }

}
