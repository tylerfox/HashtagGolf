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

  // Establishes connection with server, gets a client id assigned;
  public void handShake(String hostName, int portNum) {
  }

  private void sendInfo(Player playerInfo) {
  }

}
