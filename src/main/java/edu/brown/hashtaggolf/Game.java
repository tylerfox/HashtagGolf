package edu.brown.hashtaggolf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {
  private static final int MAX_PLAYERS = 4;

  private AtomicInteger roomReadiness;
  private Player[] savedState;
  private List<Player> savedPlayers;
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
  private static Map<Player, Long> pingTimes;

  public Game(String level, String key) throws IOException {
    roomReadiness = new AtomicInteger(0);
    players = new ArrayList<>();
    numPlayers = new AtomicInteger(0);
    ref = new Referee(level, key);
    pingTimes = new ConcurrentHashMap<>();
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

  public int getNumPlayers() {
    return numPlayers.get();
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
    System.out.println("numPlayers " + numPlayers.get());
    if (numPlayers.get() < MAX_PLAYERS) {
      int intId = numPlayers.get();
      for (int i = 0; i < numPlayers.get(); i++) {
        if (players.get(i) == null) {
          intId = i;
          break;
        }
      }

      String id = String.valueOf(intId);
      Player myPlayer = new Player(name, id, startX, startY, holeX, holeY);
      players.add(myPlayer);
      numPlayers.getAndIncrement();
      savedState = new Player[MAX_PLAYERS];
      System.out
          .println("numPlayers should be incremented " + numPlayers.get());
      return id;
    } else {
      return null;
    }
  }

  /**
   * Calculates the result of a player's swing and waits on all other players
   * before resolving.
   * @param id The ID of the player swinging.
   * @param word The word the player entered.
   * @param angle The angle of the swing.
   * @param disconnectedIds A list to be populated by the IDs of players that
   * have disconnected.
   * @return A list of copies of all players in the game with updated positions.
   */
  public List<Player> swing(int id, String word, double angle,
      List<Integer> disconnectedIds) {
    assert players.get(id) != null;
    Player myPlayer = players.get(id);

    // Save a copy of the old player
    savedState[id] = new Player(myPlayer);
    ref.swing(myPlayer, word, angle);
    myPlayer.setReady(true);

    waitUntilAllPlayersReady(disconnectedIds);

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
    if (!myPlayer.isSpectating()) {
      myPlayer.setSpectating(true);
      myPlayer.setReady(true);
      waitUntilAllPlayersReady(new ArrayList<Integer>());

      // makes copies of all players
      List<Player> newPlayers = getCopyOfPlayers();
      roomReadiness.addAndGet(1);

      return newPlayers;
    } else {
      List<Player> newPlayers = getCopyOfPlayers();
      return newPlayers;
    }
  }

  private synchronized void waitUntilAllPlayersReady(
      List<Integer> disconnectedIds) {
    boolean allPlayersReady = false;

    while (!allPlayersReady) {
      allPlayersReady = true;

      for (int i = 0; i < players.size(); i++) {
        Player player = players.get(i);
        boolean hasPlayerDisconnected = false;

        if (player != null && pingTimes.containsKey(player)
            && (pingTimes.get(player) + 10000 <= System.currentTimeMillis())) {
          hasPlayerDisconnected = true;
          disconnectedIds.add(i);
          players.set(i, null);
          decrementNumPlayers();

          System.out.println("Player " + i + " has disconnected!");
        }

        if (player != null && !player.isReady() && !hasPlayerDisconnected) {
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
      if (player != null) {
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

    List<Integer> disconnectedIds = new ArrayList<Integer>();
    waitUntilAllPlayersReady(disconnectedIds);
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

  private void savePlayers() {
    savedPlayers = getCopyOfPlayers();
  }

  public int getMaxPlayers() {
    return MAX_PLAYERS;
  }

  public List<Player> getSavedPlayers() {
    return savedPlayers;
  }

  public void clearSavedPlayers() {
    savedPlayers = null;
  }

  public void resetGame() {
    savePlayers();
    roomReadiness = new AtomicInteger(0);
    players = new ArrayList<>();
    numPlayers = new AtomicInteger(0);
  }

  public void updatePingTime(int id, long time) {
    pingTimes.put(players.get(id), time);
  }
}
