package edu.brown.networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
  private List<Player> gameState;
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
   */
  public boolean handshake(Socket socket, int id) {
    // send id back to the client
    // wait to receive player information & save info
    return true;
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
}
