package main.experiment;

import main.gameobjects.GameBoard;
import main.gameobjects.Player;
import main.ai.AI;
import main.gameobjects.Move;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class gameTrials {

  public static void runSingleGame(String p1Heuristic, String p2Heuristic, int gameNum, PrintWriter writer) {
    GameBoard board = new GameBoard();
    Player p1 = board.getP1();
    Player p2 = board.getP2();

    boolean useH2ForP1 = p1Heuristic.equals("H2");
    boolean useH2ForP2 = p2Heuristic.equals("H2");

    AI ai1 = new AI(board, useH2ForP1, 3);
    AI ai2 = new AI(board, useH2ForP2, 3);

    Player currentPlayer = p1;
    int moveCount = 0;

    while (true) {
      AI currentAI = (currentPlayer == p1) ? ai1 : ai2;
      Player opponent = (currentPlayer == p1) ? p2 : p1;

      List<Move> legalMoves = board.getLegalMoves(currentPlayer);

      if (legalMoves.isEmpty()) {
        String winner = (currentPlayer == p1) ? "P2Heuristic" : "P1Heuristic";
        writer.println(String.format("%d,%s,%s,%s,%d", gameNum, p1Heuristic, p2Heuristic, winner, moveCount));
        return;
      }

      Move move = currentAI.chooseMove(currentPlayer, opponent);

      if (move == null) {
        String winner = (currentPlayer == p1) ? "P2Heuristic" : "P1Heuristic";
        writer.println(String.format("%d,%s,%s,%s,%d", gameNum, p1Heuristic, p2Heuristic, winner, moveCount));
        return;
      }

      board.applyMove(currentPlayer, move);
      currentPlayer.setRow(move.newRow());
      currentPlayer.setCol(move.newCol());

      moveCount++;
      currentPlayer = (currentPlayer == p1) ? p2 : p1;
    }
  }

  public static void main(String[] args) {
    try (PrintWriter writer = new PrintWriter(new FileWriter("experiment_results.csv"))) {
      writer.println("#, P1Heuristic, P2Heuristic, Winner, MoveCount");

      // Experiment 1: H1 vs H1 (50 games)
      for (int i = 1; i <= 50; i++) {
        runSingleGame("H1", "H1", i, writer);
      }

      // Experiment 2: H2 vs H2 (50 games)
      for (int i = 51; i <= 100; i++) {
        runSingleGame("H2", "H2", i, writer);
      }

      // Experiment 3: H1 vs H2 (100 games, alternating)
      for (int i = 101; i <= 200; i++) {
        if (i % 2 == 1) {
          runSingleGame("H1", "H2", i, writer);
        } else {
          runSingleGame("H2", "H1", i, writer);
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}