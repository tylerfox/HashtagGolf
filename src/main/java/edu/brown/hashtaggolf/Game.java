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
  private Referee ref;
  private int startX = 0;
  private int startY = 0;
  private int holeX = 0;
  private int holeY = 0;
  private int par = 0;
  private String guihole = "";
  private boolean active = false;

  public Game(String level, String key) throws IOException {
    roomReadiness = new AtomicInteger(0);
    players = new ArrayList<>();
    numPlayers = new AtomicInteger(0);
    ref = new Referee(level, key);
  }

  public void setLevel(String level, String key, int startX, int startY,
      int holeX, int holeY, int par, String guihole) throws IOException {
    ref = new Referee(level, key);
    this.startX = startX;
    this.startY = startY;
    this.holeX = holeX;
    this.holeY = holeY;
    this.par = par;
    this.guihole = guihole;

    for (Player p : players) {
      p.setX(startX);
      p.setY(startY);
      p.setHoleX(holeX);
      p.setHoleY(holeY);
      p.setDistanceToHole(p.calcDistanceToHole());
    }
  }

  public AtomicInteger getNumPlayers() {
    return numPlayers;
  }

  public boolean isGameOver() {
    boolean gameover = true;

    for (Player player : players) {
      gameover = gameover && (player == null || player.isGameOver());
    }

    return gameover;
  }

  /**
   * Adds a player to the player list.
   * @param player to add to the game
   * @return id of player else null
   */
  public synchronized String addPlayer(String name) {
    if (numPlayers.get() < MAX_PLAYERS) {
      String id = String.valueOf(numPlayers);
      Player myPlayer = new Player(name, id, startX, startY, holeX, holeY);
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
    List<Player> newPlayers = getCopyOfPlayers();
    roomReadiness.addAndGet(1);
    return newPlayers;
  }

  public List<Player> getCopyOfPlayers() {
    List<Player> playerCopies = new ArrayList<>();

    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);

      if (player == null) {
        playerCopies.add(null);
      } else {
        playerCopies.add(new Player(player));
      }
    }

    return playerCopies;
  }

  public List<Player> spectate(int id) {
    assert players.get(id) != null;
    Player myPlayer = players.get(id);
    System.out.println(myPlayer);
    if (!myPlayer.isSpectating()) {
      myPlayer.setSpectating(true);
      myPlayer.setReady(true);
      waitUntilAllPlayersReady();

      // makes copies of all players
      List<Player> newPlayers = getCopyOfPlayers();
      roomReadiness.addAndGet(1);

      return newPlayers;
    } else {
      List<Player> newPlayers = getCopyOfPlayers();
      return newPlayers;
    }
  }

  private synchronized void waitUntilAllPlayersReady() {
    boolean allPlayersReady = false;

    while (!allPlayersReady) {
      allPlayersReady = true;

      for (int i = 0; i < players.size(); i++) {
        Player player = players.get(i);

        if (player != null && !player.isReady()) {
          allPlayersReady = false;
        }
      }

    }
  }

  public synchronized void checkResetState() {
    int activePlayers = getActivePlayerCount();

    if (roomReadiness.get() >= activePlayers) {
      setActive(true);
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
          player.setSpectating(false);
        }
        player.setReady(false);
      }
    }

    roomReadiness.set(0);

    for (Player player : players) {
      // Moves the ball back to its original positioning, since it
      // went into the water (however the front ends receives
      // where the ball went in the water)
      if (player != null){
        // if player is out, revert state back to past state
        if (player.getTerrain() == Terrain.WATER
            || player.getTerrain() == Terrain.OUT_OF_BOUNDS) {
          Player oldPlayer = savedState[Integer.parseInt(player.getId())];
          player.setX(oldPlayer.getX());
          player.setY(oldPlayer.getY());
          player.setTerrain(oldPlayer.getTerrain());
        }
      }
    }

    savedState = new Player[MAX_PLAYERS];
  }

  /**
   * Updates and returns number of players still playing.
   * @return number of players playing
   */
  private int getActivePlayerCount() {
    int count = 0;

    for (Player player : players) {
      if (player != null) {
        count++;
      }
    }

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
  }

  public void decrementNumPlayers() {
    numPlayers.getAndDecrement();
  }

  public List<Player> getPlayers() {
    return players;
  }

  public int getPar() {
    return par;
  }

  public String getGuihole() {
    return guihole;
  }

  public int getHoleX() {
    return holeX;
  }

  public int getHoleY() {
    return holeY;
  }

  public int getStartX() {
    return startX;
  }

  public int getStartY() {
    return startY;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
