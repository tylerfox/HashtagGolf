package edu.brown.hashtaggolf;

import java.io.Serializable;

/**
 * Class for players.
 */
public abstract class Player implements Serializable {
  /**
   * UID for networking.
   */
  private static final long serialVersionUID = -2317689451679552386L;
  private static final int STROKE_PENALTY = 2;
  private static final double SCALE_FACTOR = 3.5;
  private static final int WIGGLE_ROOM = 10;

  private int distanceToHole;
  private String name;
  private int stroke;
  private int x;
  private int y;
  private Terrain terrain;
  private int hole_x;
  private int hole_y;
  private boolean ready = false;
  private boolean isGameOver = false;
  private boolean outOfBounds = false;

  /**
   * Instantiates a player.
   * @param name the name of the player
   */
  public Player(String name) {
    this.name = name;
    this.terrain = Terrain.TEE;

    // TODO: will need to figure out how to set this based on the level
    this.stroke = 1;
    this.x = 310;
    this.y = 355;
    this.hole_x = 968;
    this.hole_y = 350;
    this.distanceToHole = calcDistanceToHole(); // yards
  }

  public Player(String name, int startx, int starty, int holex, int holey) {
    this.name = name;
    this.terrain = Terrain.TEE;

    // TODO: will need to figure out how to set this based on the level
    this.stroke = 1;
    this.x = startx;
    this.y = starty;
    this.hole_x = holex;
    this.hole_y = holey;
    this.distanceToHole = calcDistanceToHole(); // yards
  }

  public Player(Player p) {
    this.distanceToHole = p.distanceToHole;
    this.name = p.name;
    this.stroke = p.stroke;
    this.x = p.x;
    this.y = p.y;
    this.terrain = p.terrain;
    this.hole_x = p.hole_x;
    this.hole_y = p.hole_y;
    this.ready = p.ready;
    this.isGameOver = p.isGameOver;
    this.outOfBounds = p.outOfBounds;
  }

  // Players with powerups will override
  // this method
  /**
   * If another PlayerType overrides this method, the distance the ball goes
   * will change based on the ability.
   * @param distance the original distance the ball traveled
   * @return the new, powered-up distance
   */
  public int powerup(int distance) {
    // currently no powerup
    return distance;
  }

  // updates the ball's location
  // will increment score + 1
  /**
   * Updates the coordinates of the player to the new location.
   * @param distance the distance the ball moved
   * @param angle the angle at which the ball moves
   */
  public void moveBall(int distance, double angle) {
    System.out.println("Ball went " + distance + " yards!");
    if (distanceToHole - distance < 0) {
      System.out.println("Whoops! Overshot the hole.");
    }

    // distanceToHole = Math.abs(distanceToHole - distance);
    x += (int) (distance * Math.cos(Math.toRadians(angle)) * SCALE_FACTOR);
    y -= (int) (distance * Math.sin(Math.toRadians(angle)) * SCALE_FACTOR);

    distanceToHole = calcDistanceToHole();
    stroke++;
  }

  /**
   * Returns the distance to the hole.
   * @return rounded to the closest yard, distance to hole
   */
  public int calcDistanceToHole() {
    return (int) (Math.sqrt(Math.pow(x - hole_x, 2) + Math.pow(y - hole_y, 2)) / SCALE_FACTOR);
  }

  /**
   * Gets the current terrain the ball of the player is on.
   * @return the terrain
   */
  public Terrain getTerrain() {
    return terrain;
  }

  // if ball goes out of bounds, score increments by 3
  /**
   * Applies a stroke penalty for when the ball goes out of bounds.
   */
  public void applyStrokePenalty() {
    stroke += STROKE_PENALTY;
  }

  public void setOutOfBounds(boolean outOfBounds) {
    this.outOfBounds = outOfBounds;
  }

  public boolean isOutOfBounds() {
    return outOfBounds;
  }

  /**
   * Sets a new terrain for the player
   * @param t the new terrain
   */
  public void setTerrain(Terrain t) {
    terrain = t;
  }

  /**
   * If the ball is in the same terrain as the hole, the game is over for that
   * player.
   * @return if the game is over or not.
   */
  public boolean isGameOver() {
    isGameOver = (terrain == Terrain.HOLE || distanceToHole < WIGGLE_ROOM);
    return isGameOver;
  }

  @Override
  public String toString() {
    return "Player Info:\nName: " + name + ", Stroke #: " + stroke
        + ", Distance to Hole: " + distanceToHole;
  }

  public int getStroke() {
    return stroke;
  }

  /**
   * Sets the distance to the hole to a new value.
   * @param distanceToHole the new distance to the hole
   */
  public void setDistanceToHole(int distanceToHole) {
    this.distanceToHole = distanceToHole;
  }

  /**
   * Gets the distance to the hole.
   * @return the distance to the hole
   */
  public int getDistanceToHole() {
    return distanceToHole;
  }

  /**
   * Gets the current x coordinate of the ball.
   * @return the x coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Sets the x coordinate.
   * @param x the new value to set the x coordinate to
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Gets the current y coordinate of the ball.
   * @return the y coordinate
   */
  public int getY() {
    return y;
  }

  /**
   * Sets the y coordinate.
   * @param y the new value to set the y coordinate to
   */
  public void setY(int y) {
    this.y = y;
  }

  public boolean isReady() {
    return ready;
  }

  public void setReady(boolean ready) {
    this.ready = ready;
  }
}
