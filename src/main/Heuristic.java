package main;

public class Heuristic {
  private GameBoard board;

  public Heuristic(GameBoard board) {
    this.board = board;
  }

  public int evalH1(Player current, Player opponent, Move move) {
    int score = board.mobility(move.getNewRow(),  move.getNewCol());

    if (!(move.getTokenRow() == opponent.getRow() && move.getTokenCol() == opponent.getCol())) {
      score += board.mobility(opponent.getRow(), opponent.getCol());
    }
    return score;

  }
}
