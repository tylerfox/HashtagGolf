package edu.brown.networking;

import edu.brown.hashtaggolf.Player;

public class Client {
  private int id;
  private String hostname;
  private int portNum;

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

  public boolean sendInfo(Player playerInfo) {
    try {
      //...
      return true;
    } catch (Exception e){
      return false;
    }
  }
}
