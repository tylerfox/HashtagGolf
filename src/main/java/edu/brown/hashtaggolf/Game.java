package edu.brown.hashtaggolf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Game class which stores all information
 * relevant to a single game including
 * the players' states and the level's attributes.
 */
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

  /**
   * Sets the level in a game by changing all players' levels
   * to this level and saving attributes in Game class.
   * @param level level number
   * @param key key image file
   * @param startX x-coordinate of the ball start/tee
   * @param startY y-coordinate of the ball start/tee
   * @param holeX x-coordinate of the hole
   * @param holeY y-coordinate of the hole
   * @param par par of the level
   * @param guihole image file for the hole
   * @param scale scale factor
   * @throws IOException if IO Exception
   */
  public void setLevel(String level, String key, int startX, int startY,
      int holeX, int holeY, int par, String guihole, double scale) throws IOException {
    ref = new Referee(level, key);
    ref.setScaleFactor(scale);
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
      p.setScaleFactor(ref.getScaleFactor());
    }
  }

  /**
   * Checks in the game is over.
   * @return true if the game is over false otherwise
   */
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
      int intId = numPlayers.get();
      for (int i = 0; i < numPlayers.get(); i++) {
        if (players.get(i) == null) {
          intId = i;
          break;
        }
      }

      String id = String.valueOf(intId);
      Player myPlayer = new Player(name, id, startX, startY, holeX, holeY, ref.getScaleFactor());
      players.add(myPlayer);
      numPlayers.getAndIncrement();
      savedState = new Player[MAX_PLAYERS];
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

  /**
   * Allows players to spectate after they've won the game.
   * @param id id of player who is spectating
   * @return updated list of players and their new balls
   */
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

  /**
   * Waits until all players' ready flags are set
   * to true.
   * @param disconnectedIds list of disconnected ids
   */
  private void waitUntilAllPlayersReady(
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

  /**
   * Resets the state if it is the end of a turn.
   */
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

  /**
   * Indicates to the program the host would like to start the game.
   * Will not start until all other players are ready.
   * @param id id of the host
   * @return list of unready players - will be empty if all players
   * are ready
   */
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

  /**
   * Sets a player to ready.  Waits for
   * other players to be ready to continue.
   * @param id id of the player to set to ready
   */
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

  /**
   * Decrements the number of players.
   */
  public void decrementNumPlayers() {
    numPlayers.getAndDecrement();
  }

  /**
   * Gets the list of players (can mutate).
   * @return list of players
   */
  public List<Player> getPlayers() {
    return players;
  }

  /**
   * Gets the par.
   * @return par
   */
  public int getPar() {
    return par;
  }

  /**
   * Gets the guihole image file name.
   * @return guihole image file name
   */
  public String getGuihole() {
    return guihole;
  }

  /**
   * Gets the x-coordinate of the hole.
   * @return x-coordinate of hole
   */
  public int getHoleX() {
    return holeX;
  }

  /**
   * Gets the y-coordinate of the hole.
   * @return y-coordinate of hole
   */
  public int getHoleY() {
    return holeY;
  }

  /**
   * Gets the x-coordinate of the tee/starting ball position.
   * @return x-coordinate of tee/starting ball position
   */
  public int getStartX() {
    return startX;
  }

  /**
   * Gets the y-coordinate of the tee/starting ball position.
   * @return y-coordinate of tee/starting ball position
   */
  public int getStartY() {
    return startY;
  }

  /**
   * Gets the active boolean.
   * @return true if the game is active
   * else returns false
   */
  public boolean isActive() {
    return active;
  }

  /**
   * Sets the game to active or inactive.
   * @param active set to true if active,
   * else set to false
   */
  public void setActive(boolean active) {
    this.active = active;
  }

  private void savePlayers() {
    savedPlayers = getCopyOfPlayers();
  }

  /**
   * Gets the max number of players.
   * @return max number of players
   */
  public int getMaxPlayers() {
    return MAX_PLAYERS;
  }

  /**
   * Gets list of saved players from last game.
   * @return list of saved players
   */
  public List<Player> getSavedPlayers() {
    return savedPlayers;
  }

  /**
   * Gets the number of players currently in the game.
   * @return number of players in the game
   */
  public int getNumPlayers() {
    return numPlayers.get();
  }

  /**
   * Resets the state of the game to allow to play
   * a new level on this game.
   */
  public void resetGame() {
    savePlayers();
    roomReadiness = new AtomicInteger(0);
    players = new ArrayList<>();
    numPlayers = new AtomicInteger(0);
  }

  /**
   * Updates the ping time.
   * @param id of the client
   * @param time time of ping
   */
  public void updatePingTime(int id, long time) {
    pingTimes.put(players.get(id), time);
  }

  /**
   * Gets the scale factor from the referee.
   * @return
   */
  public double getScaleFactor() {
    return ref.getScaleFactor();
  }
}
