package main;

import java.util.*;

public class AI {
  private GameBoard board;
  private Heuristic h;

  public AI(GameBoard board) {
    this.board = board;
    this.h = new Heuristic(board);
  }

  public Move chooseMove(Player current, Player opponent) {
    List<Move> moves = board.getLegalMoves(current);
    if (moves.isEmpty()) return null;

    int bestScore = Integer.MIN_VALUE;
    List<Move> bestMoves = new ArrayList<>();

    for (Move m : moves) {
      int score = h.evalH1(current,opponent,m);
      if (score > bestScore) {
        bestScore = score;
        bestMoves.clear();
        bestMoves.add(m);
      }else if (score == bestScore) {
        bestMoves.add(m);
      }
    }
    return bestMoves.get(new Random().nextInt(bestMoves.size()));
  }
}
