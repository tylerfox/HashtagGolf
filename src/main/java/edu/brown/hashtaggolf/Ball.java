package edu.brown.hashtaggolf;

public class Ball {
	private int x;
	private int y;
	private Terrain terrain;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	// consider making copy of terrain
	public Terrain getTerrain() {
		return terrain;
	}

	// updates location of the ball
	public void updateLocation(int distance, double angle) {
	}

}