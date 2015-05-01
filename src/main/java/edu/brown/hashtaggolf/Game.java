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
  private AtomicInteger numPlayers;
  private int activePlayerCount;
  private Referee ref;

  public Game(String level, String key) throws IOException {
    roomReadiness = new AtomicInteger(0);
    players = new ArrayList<>();
    numPlayers = new AtomicInteger(0);
    activePlayerCount = 0;
    ref = new Referee(level, key);
  }

  public AtomicInteger getNumPlayers() {
    return numPlayers;
  }

  /**
   * Adds a player to the player list.
   * @param player to add to the game
   * @return id of player else null
   */
  public String addPlayer(String name) {
    if (numPlayers.get() < MAX_PLAYERS) {
      String id = String.valueOf(numPlayers);
      Player myPlayer = new Player(name, id);
      players.add(myPlayer);
      numPlayers.getAndIncrement();
      savedState = new Player[MAX_PLAYERS];

      return id;
    } else {
      return null;
    }
  }

  public List<Player> swing(int id, String word, double angle) {
    assert players.get(id) != null;
    Player myPlayer = players.get(id);

    // Save a copy of the old player
    savedState[id] = new Player(myPlayer);
    ref.swing(myPlayer, word, angle);
    myPlayer.setReady(true);

    waitUntilAllPlayersReady();

    // makes copies of all players
    List<Player> tempList = new ArrayList<>();
    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);

      if (player == null) {
        tempList.add(null);
      } else {
        tempList.add(new Player(player));
      }
    }

    roomReadiness.addAndGet(1);
    return tempList;
  }

  private void waitUntilAllPlayersReady() {
    boolean allPlayersReady = false;

    while (!allPlayersReady) {
      allPlayersReady = true;

      for (int i = 0; i < players.size(); i++) {
        Player player = players.get(i);

        if (player != null && !player.isReady()) {
          allPlayersReady = false;
        }
      }

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        System.out.println("ERROR: Issue sleeping"
            + " thread in wait until all players ready.");
      }
    }
  }

  public void checkResetState() {
    int activePlayers = getActivePlayerCount(players);
    if (roomReadiness.get() >= activePlayers) {
      resetReadinessAndState();
    }
  }

  /**
   * Sets all the readiness of all activePlayers to false.
   */
  public void resetReadinessAndState() {
    for (Player player : players) {
      if (player != null) {
        if (player.isGameOver()) {
          player.setReady(true);
        } else {
          player.setReady(false);
        }
      }
    }

    roomReadiness.set(0);

    for (Player player : players) {
      // Moves the ball back to its original positioning, since it
      // went into the water (however the front ends receives
      // where the ball went in the water)
      if (player != null && player.getTerrain() == Terrain.WATER) {
        Player oldPlayer = savedState[Integer.parseInt(player.getId())];
        player.setX(oldPlayer.getX());
        player.setY(oldPlayer.getY());
        player.setTerrain(oldPlayer.getTerrain());
      }
    }

    savedState = new Player[MAX_PLAYERS];
  }


  /**
   * Updates and returns number of players still playing.
   * @return number of players playing
   */
  private int getActivePlayerCount(List<Player> players) {
    int count = 0;

    for (Player player : players) {
      if (player != null && !player.isGameOver()) {
        count++;
      }
    }

    activePlayerCount = count;
    return count;
  }

  public List<Player> hostStart(String id) {
    Player myPlayer = players.get(Integer.parseInt(id));

    if (myPlayer != null) {
      myPlayer.setReady(true);
    }

    boolean allOtherPlayersReady = true;
    List<Player> unreadyPlayers = new ArrayList<>();

    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);

      if (player != null && !player.isReady()) {
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

    if (myPlayer != null) {
      myPlayer.setReady(true);
    }

    waitUntilAllPlayersReady();
    roomReadiness.addAndGet(1);
    System.out.println("Done waiting for all players.");
  }

  public int getActivePlayerCount() {
    return activePlayerCount;
  }

  public void decrementNumPlayers() {
    numPlayers.getAndDecrement();
  }

  public List<Player> getPlayers() {
    return players;
  }
}
