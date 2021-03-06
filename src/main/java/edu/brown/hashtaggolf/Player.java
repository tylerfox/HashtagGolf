package edu.brown.hashtaggolf;

/**
 * Class for players.
 */
public class Player {

  private static final int STROKE_PENALTY = 1;
  private static final int WIGGLE_ROOM = 15;

  public static final int UNSET_COLOUR = 0;
  public static final int RED_BALL = 1;
  public static final int BLUE_BALL = 2;
  public static final int YELLOW_BALL = 3;
  public static final int GREEN_BALL = 4;

  private int distanceToHole;
  private String name;
  private int stroke;
  private int x;
  private int y;
  private Terrain terrain;
  private int hole_x;
  private int hole_y;
  private int ballColor;
  private boolean ready;
  private boolean isGameOver = false;
  private boolean outOfBounds;
  private String id;
  private boolean spectating = false;
  private boolean repl = false;
  private double scaleFactor = 1.0;

  /**
   * Instantiates a player without knowledge of a level.
   * @param name the name of the player
   * @param id id of the player
   */
  public Player(String name, String id) {
    this.name = name;
    this.id = id;
    this.terrain = Terrain.TEE;

    this.stroke = 1;
    this.x = 0;
    this.y = 0;
    this.hole_x = 0;
    this.hole_y = 0;
    this.distanceToHole = 5000;// calcDistanceToHole(); // yards
    this.ballColor = UNSET_COLOUR;
    this.ready = false;
    this.isGameOver = false;
    this.outOfBounds = false;
  }

  /**
   * Creates a Player with information about level.
   * @param name name of player
   * @param id id of player
   * @param startx x-coordinate of start position
   * @param starty y-coordinate of start position
   * @param holex x-coordinate of hole
   * @param holey y-coordinate of hole
   * @param scale scale factor
   */
  public Player(String name, String id, int startx, int starty, int holex,
      int holey, double scale) {
    this.name = name;
    this.id = id;
    this.terrain = Terrain.TEE;
    this.stroke = 1;
    this.x = startx;
    this.y = starty;
    this.hole_x = holex;
    this.hole_y = holey;
    this.distanceToHole = calcDistanceToHole(); // yards
    this.scaleFactor = scale;
  }

  /**
   * For creating a copy of the player.
   * @param p player
   */
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
    this.id = p.id;
  }

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

  /**
   * Updates the coordinates of the player's ball to the new location.
   * Increments score + 1.
   * @param distance the distance the ball moved
   * @param angle the angle at which the ball moves
   */
  public void moveBall(int distance, double angle) {
    if (repl) {
      System.out.println("Ball went " + distance + " yards!");

      if (distanceToHole - distance < 0) {
        System.out.println("Whoops! Overshot the hole.");
      }
    }

    distanceToHole = Math.abs(distanceToHole - distance);
    x += (int) (distance * Math.cos(Math.toRadians(angle)) * scaleFactor);
    y -= (int) (distance * Math.sin(Math.toRadians(angle)) * scaleFactor);

    distanceToHole = calcDistanceToHole();
    stroke++;
  }

  /**
   * Returns the distance to the hole.
   * @return rounded to the closest yard, distance to hole
   */
  public int calcDistanceToHole() {
    return (int) (Math.sqrt(Math.pow(x - hole_x, 2) + Math.pow(y - hole_y, 2)) / scaleFactor);
  }

  /**
   * Gets the current terrain the ball of the player is on.
   * @return the terrain
   */
  public Terrain getTerrain() {
    return terrain;
  }

  /**
   * Applies a stroke penalty for when the ball goes out of bounds.
   */
  public void applyStrokePenalty() {
    stroke += STROKE_PENALTY;
  }

  /**
   * Adds value to stroke number.
   * @param num to be added to stroke number
   */
  public void addStroke(int num) {
    stroke += num;
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
   * Sets the hole_x coordinate.
   * @param hole_x the new value to set the hole_x coordinate to
   */
  public void setHoleX(int hole_x) {
    this.hole_x = hole_x;
  }

  /**
   * Sets the hole_y coordinate.
   * @param hole_y the new value to set the hole_y coordinate to
   */
  public void setHoleY(int hole_y) {
    this.hole_y = hole_y;
  }

  /**
   * Sets the y coordinate.
   * @param y the new value to set the y coordinate to
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * Checks readiness of the player.
   * @return true if ready else false
   */
  public boolean isReady() {
    return ready;
  }

  /**
   * Sets the player to ready or not ready.
   * @param ready true if ready else false
   */
  public void setReady(boolean ready) {
    this.ready = ready;
  }

  /**
   * Gets the ball color.
   * @return ball color
   */
  public int getBallColor() {
    return ballColor;
  }

  /**
   * Sets the ball color.
   * @param ballColor color of the ball
   */
  public void setBallColor(int ballColor) {
    this.ballColor = ballColor;
  }

  /**
   * Sets the id of the player.
   * @param id id of player
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the ID of the player.
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the name of the player.
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets spectating boolean.
   * @return true if spectating set to true else false
   */
  public boolean isSpectating() {
    return spectating;
  }

  /**
   * Sets spectating.
   * @param spectating true if spectacting else false
   */
  public void setSpectating(boolean spectating) {
    this.spectating = spectating;
  }

  /**
   * Tells program to run repl if repl is set to true.
   */
  public void setRepl(boolean repl) {
    this.repl = repl;
  }

  /**
   * Sets the scale factor.
   * @param scale scale factor
   */
  public void setScaleFactor(double scale) {
    scaleFactor = scale;
  }
}
