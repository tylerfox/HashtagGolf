package edu.brown.networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
//import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
//import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import edu.brown.hashtaggolf.Player;
import edu.brown.hashtaggolf.PlayerType1;

/**
 * The server side of networking.
 */
public class Server {
 // private List<Player> gameState;
  private ServerSocket myServer;
  private List<Socket> clients;

  /**
   * Instantiates a new Server.
   * @param portNum the port number to connect to
   * @throws IOException 
   */
  public Server(int portNum) throws IOException {
    myServer = new ServerSocket(portNum);
  }

  // runs server until all Players have connected

  /**
   * Runs the server.
   * @throws IOException 
   */
  public void run() throws IOException {
    clients = new ArrayList<>();

    while (clients.size() < 4) {  // and host client doesn't stop accepting
      if (myServer == null) {
        break;
      } else {
        System.out.println("Accepting new client.");
        Socket socket = myServer.accept();
        clients.add(socket);
        handshake(socket, clients.size());

        if (clients.size() > 1) {
          System.out.println("Would you like to start playing?");
        }
      }

    }

    // waits for handshake from all Players (until Host sends start game)
  }

  /**
   * Creates socket for the client, opens input and output streams
   * Adds socket for client to the clients list
   * Sends the client id back to the client.
   * @param socket socket to handshake with
   * @param id client id (index of Player in the clients list)
   * @return True on successful handshake, false otherwise
   * @throws IOException 
   */
  public boolean handshake(Socket socket, int id) throws IOException {
    BufferedWriter client = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    BufferedReader clientResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    client.write(id);
    String response = clientResponse.readLine();
    System.out.println(response);
    // send id back to the client
    // wait to receive player information & save info
    return (response != null);
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
      for (Socket socket : clients){
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(new PlayerType1("Beverly")); // send all Players... list of Players?
        oos.close();
      }
      return true;
    } catch (Exception e){
      return false;
    }
  }
  
  public static void main(String[] args) {
    try {
      Server server = new Server(1234);
      server.run();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
