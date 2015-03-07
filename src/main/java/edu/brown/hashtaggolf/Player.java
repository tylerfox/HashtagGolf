package edu.brown.hashtaggolf;

public abstract class Player {

  String name;
  int playerType;
  State currentState;

  public State move(String query) {
    return null;
  }
}
