package edu.brown.networking;

import java.net.Socket;
import java.util.List;

import edu.brown.hashtaggolf.Player;

public class Server {
  private List<Player> gameState;
  private List<Socket> clients;
  private int portNum;
  
  public Server(int portNum) {
    this.portNum = portNum;
  }

  // runs server until all Players have connected
  public void run() {
  	// waits for handshake from all Players (until Host sends start game)
  }
  
  /**
   * Creates socket for the client, opens input and output streams
   * Adds socket for client to the clients list
   * Sends the client id back to the client
   * @return True on successful handshake, false otherwise
   */
  public boolean handshake() {
    try {
      //...
      return true;
    } catch (Exception e){
      return false;
    }
  }

  // updates the state of the player with the given id
  public boolean updateState(int id, Player player) {
    try {
      //...
      return true;
    } catch (Exception e){
      return false;
    }
  }

  // sends new state to all clients
  public boolean sendState() {
    try {
      //...
      return true;
    } catch (Exception e){
      return false;
    }
  }
  
  public int getPort() {
    return portNum;
  }
}
