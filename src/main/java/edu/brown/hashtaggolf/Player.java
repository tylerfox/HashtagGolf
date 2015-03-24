package edu.brown.hashtaggolf;

public abstract class Player {
  private int distanceToHole;
  private String name;
  private int stroke;
  private int x;
  private int y;
  private Terrain terrain;

  public Player(String name) {
  	this.name = name;
  	this.terrain = Terrain.TEE;
  	
  	// TODO: will have to figure out how to set this based on the level
  	this.distanceToHole = 30;
  	this.stroke = 1;
  	this.x = 0;
  	this.y = 0;
  }

 
  // Players with powerups will override
  // this method
  public int powerup(int distance) {
    // currently no powerup
  	return distance;
  }

  // updates the ball's location
  // will increment score + 1
  public void moveBall(int distance, double angle) {
    System.out.println("Ball went " + distance + " yards!");
    if (distanceToHole - distance < 0) {
      System.out.println("Whoops! Overshot the hole.");
    }
    distanceToHole = Math.abs(distanceToHole - distance);
    x += distance;
  	stroke++;
  }
  
  public Terrain getTerrain() {
    return terrain;
  }

  // if ball goes out of bounds, score increments by 3
  public void outOfBounds() {
  	stroke += 3;
  }

  public void setTerrain(Terrain t) {
    terrain = t;
  }

  public boolean isGameOver() {
    return terrain == Terrain.HOLE;
  }

  @Override
  public String toString() {
    return "Player Info:\nName: " + name + " Stroke #: " + stroke + " Distance to Hole: " + distanceToHole;
  }

  public void setDistanceToHole(int distanceToHole) {
    this.distanceToHole = distanceToHole;
  }

  public int getDistanceToHole() {
    return distanceToHole;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }
}
