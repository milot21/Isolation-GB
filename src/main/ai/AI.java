package main.ai;

import main.gameobjects.GameBoard;
import main.gameobjects.GameState;
import main.gameobjects.Move;
import main.gameobjects.Player;

import java.util.*;

public class AI {
  private final GameBoard board;
  private final Heuristic h;
  private boolean useH2;
  private int maxDepth;

  public AI(GameBoard board, boolean useH2, int maxDepth) {
    this.board = board;
    this.h = new Heuristic(board);
    this.useH2 = useH2;
    this.maxDepth = maxDepth;
  }

  //simple constructor to use depth 3
  public AI(GameBoard board, boolean useH2) {
    this(board, useH2, 3);
  }

  public Move chooseMove(Player current, Player opponent) {
    List<Move> moves = board.getLegalMoves(current);
    if (moves.isEmpty()) return null;

    Collections.shuffle(moves);

    int bestScore = Integer.MIN_VALUE;
    List<Move> bestMoves = new ArrayList<>();

    int alpha = Integer.MIN_VALUE;
    int beta = Integer.MIN_VALUE;

    for (Move m : moves) {
      GameState s = new GameState(board, current, opponent);
      board.applyMove(current, m);
      int score = minimax(current, opponent, maxDepth - 1, alpha, beta, false);

      s.restore(board, current, opponent);

      if (score > bestScore) {
        bestScore = score;
        bestMoves.clear();
        bestMoves.add(m);
      } else if (score == bestScore) {
        bestMoves.add(m);
      }
      alpha = Math.max(alpha, bestScore);
    }
    return bestMoves.get(new Random().nextInt(bestMoves.size()));
  }

  private int minimax(Player curr, Player opp, int depth, int alpha, int beta, boolean isMaxing) {
    List<Move> moves = board.getLegalMoves(curr);
    if (moves.isEmpty()) {
      return isMaxing ? Integer.MIN_VALUE + 1 : Integer.MAX_VALUE - 1;
    }

    if (depth == 0) {
      return evaluatePos(curr, opp);
    }

    Collections.shuffle(moves);

    if (isMaxing) {
      int max = Integer.MIN_VALUE;

      for (Move m : moves) {
        GameState state = new GameState(board, curr, opp);
        board.applyMove(curr, m);

        int score = minimax(opp, curr, depth - 1, alpha, beta, false);
        state.restore(board, curr, opp);
        max = Math.max(max, score);
        alpha = Math.max(alpha, score);

        if (beta <= alpha) {
          break;
        }
      }
      return max;
    } else {
      int min = Integer.MAX_VALUE;

      for (Move m : moves) {
        GameState state = new GameState(board, curr, opp);
        board.applyMove(curr, m);
        int score = minimax(opp, curr, depth - 1, alpha, beta, true);
        state.restore(board, curr, opp);
        min = Math.min(min, score);
        beta = Math.min(beta, score);
        if (beta <= alpha) {
          break;
        }
      }
      return min;
    }
  }

  private int evaluatePos(Player aiPlayer, Player oppPlayer) {
    List<Move> aiMoves = board.getLegalMoves(aiPlayer);
    List<Move> oppMoves = board.getLegalMoves(oppPlayer);

    // Terminal states
    if (aiMoves.isEmpty()) return Integer.MIN_VALUE + 1;
    if (oppMoves.isEmpty()) return Integer.MAX_VALUE - 1;

    // Evaluate based on which heuristic we're using
    int totalScore = 0;
    int samplesToEval = Math.min(3, aiMoves.size());

    for (int i = 0; i < samplesToEval; i++) {
      Move m = aiMoves.get(i);
      int score = useH2 ? h.evalH2(aiPlayer, oppPlayer, m)
          : h.evalH1(aiPlayer, oppPlayer, m);
      totalScore += score;
    }

    return totalScore / samplesToEval;
  }
}
