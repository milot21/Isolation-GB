package main.ai;

import main.gameobjects.GameBoard;
import main.gameobjects.Move;
import main.gameobjects.Player;

public class Heuristic {
  private final GameBoard board;

  public Heuristic(GameBoard board) {
    this.board = board;
  }

  public int evalH1(Player current, Player opponent, Move move) {
    int currMobility = board.mobility(current.getRow(),  current.getCol());
    int nextMobility = board.mobility(move.newRow(), move.newCol());
    int hMove = 0;
    if (currMobility - nextMobility > 0) hMove = 1;
    if (currMobility - nextMobility < 0) hMove = -1;

    int oppBefore = board.mobility(opponent.getRow(), opponent.getCol());
    int oppAfter = board.mobility(opponent.getRow(), opponent.getCol(), move.tokenRow(), move.tokenCol());

    int hToken;
    if (oppAfter == 0) {
      hToken = 100; //winner
    } else if (oppAfter < oppBefore) {
      hToken = 2; // mobility decreased
    } else {
      hToken = 0; //still same
    }
    return hMove + hToken;
  }

  public int evalH2(Player current, Player opponent, Move move) {
    int currMobility = board.mobility(current.getRow(),  current.getCol());
    int nextMobility = board.mobility(move.newRow(), move.newCol());
    int hMove = currMobility - nextMobility;

    int oppAfter = board.mobility(opponent.getRow(), opponent.getCol(), move.tokenRow(), move.tokenCol());
    int hToken;
    if (oppAfter == 0) {
      hToken = 100;
    } else {
      hToken = board.countSafeCells(move.tokenRow(), move.tokenCol());
    }
    return hMove + hToken;
  }

}
