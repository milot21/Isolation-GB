
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
    for (int i = 0; i<ROWS; i++) {
      for (int j = 0; j < COLS; j++) {
        if (board[i][j].getPlayer() != null) {
          System.out.print(board[i][j].getPlayer().getId() + " ");
        } else if (board[i][j].isRemoved()) {
          System.out.print(" X ");
        } else {
          System.out.print(" O ");
        }
      }
      System.out.println();
    }
  }
}
