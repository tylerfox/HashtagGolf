package edu.brown.networking;

import java.net.Socket;
import java.util.List;

import edu.brown.hashtaggolf.Player;

/**
 * The server side of networking.
 */
public class Server {
  private List<Player> gameState;
  private List<Socket> clients;
  private int portNum;

  /**
   * Instantiates a new Server.
   * @param portNum the port number to connect to
   */
  public Server(int portNum) {
    this.portNum = portNum;
  }

  // runs server until all Players have connected
  /**
   * Runs the server.
   */
  public void run() {
  	// waits for handshake from all Players (until Host sends start game)
  }
  
  /**
   * Creates socket for the client, opens input and output streams
   * Adds socket for client to the clients list
   * Sends the client id back to the client.
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
  /**
   * updates the state of the player with the given id.
   * @param id the id
   * @param player the player to update
   * @return if the state was updated or not
   */
  public boolean updateState(int id, Player player) {
    try {
      //...
      return true;
    } catch (Exception e){
      return false;
    }
  }

  // sends new state to all clients
  /**
   * Sends new state to all clients.
   * @return if the state was sent or not
   */
  public boolean sendState() {
    try {
      //...
      return true;
    } catch (Exception e){
      return false;
    }
  }

  /**
   * Gets the port number of the server.
   * @return the port number
   */
  public int getPort() {
    return portNum;
  }
}
