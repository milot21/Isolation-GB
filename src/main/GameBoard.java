package main;

import java.util.*;

public class GameBoard {
  private final int ROWS = 8;
  private final int COLS = 6;
  private Cell[][] board;
  private Player p1, p2;

  /**
   * Creates the Cells in the board game
   * sets the players to their starting cell
   * */
  public GameBoard() {
    board = new Cell[ROWS][COLS];
    for (int i = 0; i<ROWS; i++) {
      for (int j = 0; j <COLS; j++) {
        board[i][j] = new Cell(i,j);
      }
    }
    p1 = new Player(0,3,"p1");
    p2 = new Player(7,3,"p2");
    board[p1.getRow()][p1.getCol()].setPlayer(p1);
    board[p2.getRow()][p2.getCol()].setPlayer(p2);
  }

  /**
   * Simple Board print in terminal
   * prints X is our tokens are removed
   * prints O for all available cells with tokens to move
   * */
  public void printBoard() {
    System.out.print("   ");
    for (int j = 0; j < COLS; j++) {
      System.out.print(j + " ");
    }
    System.out.println();

    for (int i = 0; i < ROWS; i++) {
      System.out.print(i + ": ");
      for (int j = 0; j < COLS; j++) {
        if (board[i][j].getPlayer() != null) {
          System.out.print(board[i][j].getPlayer().getId() + " ");
        } else if (board[i][j].isRemoved()) {
          System.out.print("X ");
        } else {
          System.out.print("O ");
        }
      }
      System.out.println();
    }
  }

  /**
   * Gets a players list of moves that it can make
   * @param player
   * @return
   */
  public List<Move> getLegalMoves(Player player) {
    List<Move> moves = new ArrayList<>();
    if (player == null) return moves;

    int row = player.getRow();
    int col = player.getCol();

    for (int dr = -1; dr <= 1; dr++) {
      for (int dc = -1; dc <= 1; dc++) {
        if (dr == 0 && dc == 0) continue;
        int newRow = row + dr;
        int newCol = col + dc;

        if (isInBounds(newRow, newCol)
            && !board[newRow][newCol].isRemoved()
            && board[newRow][newCol].getPlayer() == null) {

          // for that pawn move, choose any valid token to remove
          for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
              if (!board[r][c].isRemoved()
                  && board[r][c].getPlayer() == null
                  && !(r == newRow && c == newCol)) {
                moves.add(new Move(newRow, newCol, r, c));
              }
            }
          }
        }
      }
    }
    return moves;
  }

  private boolean isInBounds(int row, int col) {
    return row >= 0 && row < ROWS && col >= 0 && col < COLS;
  }

  public int mobility(int row, int col) {
    int count = 0;
    int[] dir = {-1, 0, 1};
    for (int dr : dir) {
      for (int d : dir) {
        if (dr == 0 && d == 0)continue;
        int newRow = row + dr;
        int newCol = col + d;
        if (isInBounds(newRow, newCol) &&
            !board[newRow][newCol].isRemoved() &&
            board[newRow][newCol].getPlayer() == null) {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * Applies the move to the player
   * gets rid of token
   * nullifies past location
   * @param player
   * @param move
   */
  public void applyMove(Player player, Move move) {
    board[player.getRow()][player.getCol()].setPlayer(null); // previous cell has no player
    board[move.getNewRow()][move.getNewCol()].setPlayer(player); //move to new pos
    board[move.getTokenRow()][move.getTokenCol()].removeToken();  // take out a token
  }
  //Getters for acquiring info
  public Player getP1() {
    return p1;
  }
  public Player getP2() {
    return p2;
  }
  public int getRows() {
    return ROWS;
  }
  public int getCols() {
    return COLS;
  }
  public Cell getCell(int row, int col) {
    return board[row][col];
  }
  public boolean hasToken(int row, int col) {
    if (row < 0 || row >= getRows() || col < 0 || col >= getCols()) {
      return false; // out of bounds
    }
    Cell cell = getCell(row, col);
    return !cell.isRemoved() && cell.getPlayer() == null;
  }

}
