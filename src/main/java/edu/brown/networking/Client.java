package edu.brown.networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.brown.hashtaggolf.Player;

/**
 * The Client side of the networking.
 */
public class Client {
  private int id;
  private String hostname;
  private int portNum;
  private Socket mySocket;

  /**
   * Instantiates a new Client.
   * @param hostname the name of the host of the client
   * @param portNum the port number of the client
   * @throws IOException 
   * @throws UnknownHostException 
   */
  public Client (String hostname, int portNum) throws UnknownHostException, IOException {
  	this.hostname = hostname;
  	this.portNum = portNum;
  	this.mySocket = new Socket(hostname, portNum);
  	handshake();
  }

  /**
   * Establishes connection with server, gets a client ID assigned.
   * @param hostName The name of the host server.
   * @param portNum The port number.
   * @return True on successful handshake, false otherwise.
   */
  public boolean handshake() {
    try {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
      
      id = Integer.parseInt(reader.readLine());
      writer.write("Player1");
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
  
  public void receiveInfo() throws IOException, ClassNotFoundException {
    Socket socket = new Socket();
    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
    Object o = ois.readObject();
    if(o instanceof Player) {
       Player ds = (Player)o;
       System.out.println(ds.toString());
    }
    else {
       // something gone wrong - this should not happen if your
       // socket is connected to the sending side above.
    }
  }
  
  public static void main(String[] args) {
    try {
      Client client = new Client("localhost", 1234);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
