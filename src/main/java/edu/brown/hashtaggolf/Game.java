package edu.brown.hashtaggolf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {

  private static final int MAX_PLAYERS = 4;
  private AtomicInteger roomReadiness;
  private Player[] savedState;
  private List<Player> players;
  private int numPlayers;
  private int activePlayerCount;
  private Referee ref;

  public Game(String level, String key) throws IOException {
    roomReadiness = new AtomicInteger(0);
    players = new ArrayList<>();
    numPlayers = 0;
    activePlayerCount = 0;
    ref = new Referee(level, key);
  }

  public int getNumPlayers() {
    return numPlayers;
  }

  /**
   * Adds a player to the player list.
   * @param player to add to the game
   * @return id of player else null
   */
  public String addPlayer(String name) {
    if (numPlayers < MAX_PLAYERS) {
      String id = String.valueOf(numPlayers);
      Player myPlayer = new Player(name, id);
      players.add(myPlayer);
      numPlayers++;
      savedState = new Player[numPlayers];
      return id;
    } else {
      return null;
    }
  }

  public List<Player> swing(int id, String word, double angle) {
    assert players.get(id) != null;
    Player myPlayer = players.get(id);

    // Save a copy of the old player
    savedState[Integer.parseInt(myPlayer.getId())] = new Player(myPlayer);
    ref.swing(myPlayer, word, angle);
    myPlayer.setReady(true);

    waitUntilAllPlayersReady();

    // makes copies of all players
    List<Player> tempList = new ArrayList<>();
    for (int i = 0; i < players.size(); i++) {
      tempList.add(new Player(players.get(i)));
    }
    return tempList;
  }

  private void waitUntilAllPlayersReady() {
    boolean allPlayersReady = false;

    while (!allPlayersReady) {
      allPlayersReady = true;

      for (int i = 0; i < players.size(); i++) {
        if (!players.get(i).isReady()) {
          allPlayersReady = false;
        }
      }
    }

    roomReadiness.addAndGet(1);
  }

  public void checkResetState() {
    if (roomReadiness.get() == getActivePlayerCount(players)) {
      resetReadinessAndState();
      System.out.println("resetting readiness and state");
    }
  }

  /**
   * Sets all the readiness of all activePlayers to false.
   */
  public void resetReadinessAndState() {

    for (Player player : players) {
      if (player.isGameOver()) {
        player.setReady(true);
      } else {
        player.setReady(false);
      }
    }
    roomReadiness.set(0);

    for (Player player : players) {
      // Moves the ball back to its original positioning, since it
      // went into the water (however the front ends receives
      // where the ball went in the water)
      if (player.getTerrain() == Terrain.WATER) {
        Player oldPlayer = savedState[Integer.parseInt(player.getId())];
        player.setX(oldPlayer.getX());
        player.setY(oldPlayer.getY());
        player.setTerrain(oldPlayer.getTerrain());
      }
      savedState = new Player[numPlayers];
    }

  }


  /**
   * Updates and returns number of players still playing.
   * @return number of players playing
   */
  private int getActivePlayerCount(List<Player> players) {
    int count = 0;

    for (Player player : players) {
      if (!player.isGameOver()) {
        count++;
      }
    }
    activePlayerCount = count;
    return count;
  }

  public List<Player> hostStart(String id) {
    Player myPlayer = players.get(Integer.parseInt(id));
    myPlayer.setReady(true);

    boolean allOtherPlayersReady = true;
    List<Player> unreadyPlayers = new ArrayList<>();

    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);
      if (!player.isReady()) {
        unreadyPlayers.add(player);
        allOtherPlayersReady = false;
      }
    }

    if (!allOtherPlayersReady) {
      myPlayer.setReady(false);
    } else {
      roomReadiness.addAndGet(1);
    }

    return unreadyPlayers;
  }

  public void playerReady(String id) {
    int numId = Integer.parseInt(id);
    Player myPlayer = players.get(numId);
    myPlayer.setReady(true);

    waitUntilAllPlayersReady();
  }

  public int getActivePlayerCount() {
    return activePlayerCount;
  }

  public List<Player> getPlayers() {
    return players;
  }

}
