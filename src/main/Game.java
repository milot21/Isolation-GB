package main;

import main.ai.AI;
import main.gameobjects.GameBoard;
import main.gameobjects.Move;
import main.gameobjects.Player;

import java.util.List;
import java.util.Scanner;

public class Game {
  public static void main(String[] args) {
    GameBoard board = new GameBoard();
    AI ai = new AI(board, false);
    Scanner sc = new Scanner(System.in);

    Player curr = board.getP1(); //human
    Player opp = board.getP2();  //AI

    while (true) {
      board.printBoard();
      List<Move> moves = board.getLegalMoves(curr);
      if (moves.isEmpty()) {
        System.out.println("You lost!");
        break;
      }

      if (curr == board.getP1()) {
        //human turn
        System.out.println("Enter your moves in this order: Row, Col, tokenRow, tokenCol!");
        int row = sc.nextInt();
        int col = sc.nextInt();
        int tokenRow = sc.nextInt();
        int tokenCol = sc.nextInt();
        Move chosen = new Move(row, col, tokenRow, tokenCol);
        board.applyMove(curr, chosen);
      } else  {
        Move aiMove = ai.chooseMove(curr, opp);
        if (aiMove == null) {
          System.out.println("AI has no moves, you win!");
          break;
        }
        board.applyMove(curr, aiMove);
        System.out.println("AI moved: "+ aiMove);
      }

      Player temp = curr;
      curr = opp;
      opp = temp;
    }
    sc.close();
  }
}
