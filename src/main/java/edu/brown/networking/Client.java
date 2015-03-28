package edu.brown.networking;

import edu.brown.hashtaggolf.Player;

/**
 * The Client side of the networking.
 */
public class Client {
  private int id;
  private String hostname;
  private int portNum;

  /**
   * Instantiates a new Client.
   * @param hostname the name of the host of the client
   * @param portNum the port number of the client
   */
  public Client (String hostname, int portNum) {
  	this.hostname = hostname;
  	this.portNum = portNum;
  }

  /**
   * Establishes connection with server, gets a client ID assigned.
   * @param hostName The name of the host server.
   * @param portNum The port number.
   * @return True on successful handshake, false otherwise.
   */
  public boolean handshake(String hostName, int portNum) {
    try {
      //...
      return true;
    } catch (Exception e){
      return false;
    }
  }

  /**
   * Sends information to the player.
   * @param playerInfo the player to send information to
   * @return if the information was sent
   */
  public boolean sendInfo(Player playerInfo) {
    try {
      //...
      return true;
    } catch (Exception e){
      return false;
    }
  }
}
