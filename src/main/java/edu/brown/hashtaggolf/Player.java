package edu.brown.hashtaggolf;

/**
 * Class for players.
 *
 */
public abstract class Player {
  private int distanceToHole;
  private String name;
  private int stroke;
  private int x;
  private int y;
  private Terrain terrain;

  /**
   * Instantiates a player.
   * @param name the name of the player
   */
  public Player(String name) {
  	this.name = name;
  	this.terrain = Terrain.TEE;
  	
  	// TODO: will need to figure out how to set this based on the level
  	this.distanceToHole = 150;
  	this.stroke = 1;
  	this.x = 0;
  	this.y = 0;
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
    distanceToHole = Math.abs(distanceToHole - distance);
    //x += distance * Math.cos(angle);
    //y += distance * Math.sin(angle);
    x += distance;
  	stroke++;
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
   * Performs whatever action necessary if the ball goes out of bounds.
   */
  public void outOfBounds() {
  	stroke += 3;
  }

  /**
   * Sets a new terrain for the player
   * @param t the new terrain
   */
  public void setTerrain(Terrain t) {
    terrain = t;
  }

  /**
   * If the ball is in the same terrain as the hole,
   * the game is over for that player.
   * @return if the game is over or not.
   */
  public boolean isGameOver() {
    return terrain == Terrain.HOLE || distanceToHole < 5;
  }

  @Override
  public String toString() {
    return "Player Info:\nName: " + name + " Stroke #: " + stroke + " Distance to Hole: " + distanceToHole;
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
}
