package main;

public class Heuristic {
  private GameBoard board;

  public Heuristic(GameBoard board) {
    this.board = board;
  }

  public int evalH1(Player current, Player opponent, Move move) {
    int currMobility = board.mobility(current.getRow(),  current.getCol());
    int nextMobility = board.mobility(move.getNewRow(), move.getNewCol());
    int hMove = 0;
    if (currMobility - nextMobility > 0) hMove = 1;
    if (currMobility + nextMobility < 0) hMove = -1;

    int oppBefore = board.mobility(opponent.getRow(), opponent.getCol());
    int oppAfter = simulateOpponentMobility(opponent,move);

    int hToken;
    if (oppAfter == 0) {
      hToken = 100; //winner
    } else if (oppAfter < oppBefore) {
      hToken = 2; // mobility decreased
    } else {
      hToken = 0;
    }
    return hMove + hToken;
  }

  public int evalH2(Player current, Player opponent, Move move) {
    int currMobility = board.mobility(current.getRow(),  current.getCol());
    int nextMobility = board.mobility(move.getNewRow(), move.getNewCol());
    int hMove = currMobility - nextMobility;

    int oppAfter = simulateOpponentMobility(opponent,move);
    if (oppAfter == 0) {
      return hMove + 100;
    }

    int hToken = 0;
    for (int r = 0; r < board.getRows(); r++) {
      for (int c = 0; c < board.getCols(); c++) {
        if (board.hasToken(r,c) && board.mobility(r, c) >= 3) {
          hToken++;
        }
      }
    }
    return hMove + hToken;
  }

  private int simulateOpponentMobility(Player opp, Move m) {
    if (m.getTokenRow() == opp.getRow() && m.getTokenCol() == opp.getCol()) {
      return 0; //trapped
    }
    return board.mobility(opp.getRow(), opp.getCol());
  }

}
