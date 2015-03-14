package edu.brown.hashtaggolf;

public abstract class Player {

  private String name;
  private Ball ball;
  private int score;

  public Player(String name) {
  	this.name = name;
  }

  // Players with powerups will override
  // this method
  public int powerUp(int distance) {
  	return distance;
  }

  // updates the ball's location
  // will increment score + 1
  public void moveBall(int distance, double angle) {
  	ball.updateLocation(distance, angle);
  	score++;
  }

  // if ball goes out of bounds, score increments by 3
  public void outOfBounds() {
  	score += 3;
  }

  // returns clone of ball
  public Ball getBall() {
  	return ball;
  }
}
