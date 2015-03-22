package edu.brown.hashtaggolf;

public abstract class Player {
  private int distanceToHole;
  private String name;
  private int score;
  private int x;
  private int y;
  private Terrain terrain;

  public Player(String name) {
  	this.name = name;
  	this.terrain = Terrain.TEE;
  	
  	// TODO: will have to figure out how to set this based on the level
  	this.distanceToHole = 250;
  	this.score = 0;
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
    distanceToHole -= distance;
    x += distance;
  	score++;
  }
  
  public Terrain getTerrain() {
    return terrain;
  }

  // if ball goes out of bounds, score increments by 3
  public void outOfBounds() {
  	score += 3;
  }
  
  public void setTerrain(Terrain t) {
    terrain = t;
  }
  
  public boolean isGameOver() {
    return terrain == Terrain.HOLE;
  }
 
  
  @Override
  public String toString() {
    return "Player Info:\nName: " + name + " Stroke #: " + score + " Distance to Hole: " + distanceToHole;
  }


  public void setDistanceToHole(int distanceToHole) {
    this.distanceToHole = distanceToHole;
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
