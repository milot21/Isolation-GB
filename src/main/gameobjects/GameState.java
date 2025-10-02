package main.gameobjects;

public class GameState {
  private int p1Row, p1Col, p2Row, p2Col;
  private boolean[][] removedCells;
  private Player[][] playerCells;


  /**
   * Downloads all the data of the game state before the AI does it's next move search
   * which it uses the board but we restore it once its done
   * @param board original board
   * @param p1 player one
   * @param p2 player two/ AI
   */
  public GameState(GameBoard board, Player p1, Player p2) {
    this.p1Row = p1.getRow();
    this.p1Col = p1.getCol();
    this.p2Row = p2.getRow();
    this.p2Col = p2.getCol();
    this.removedCells = new boolean[board.getRows()][board.getCols()];
    this.playerCells = new Player[board.getRows()][board.getCols()];

    for (int r = 0; r < board.getRows(); r++) {
      for (int c = 0; c < board.getCols(); c++) {
        removedCells[r][c] = board.getCell(r,c).isRemoved();
        playerCells[r][c] = board.getCell(r,c).getPlayer();
      }
    }
  }

  /**
   * restores the gameboard to its current moves, after AI does beta-pruning or minimax
   * simulations with either heuristic
   * @param b board
   * @param p1 player one
   * @param p2 player two or AI
   */
  public void restore(GameBoard b, Player p1, Player p2) {
    p1.setRow(p1Row);
    p1.setCol(p1Col);
    p2.setRow(p2Row);
    p2.setCol(p2Col);

    for (int r = 0; r < b.getRows(); r++) {
      for (int c = 0; c < b.getCols(); c++) {
        b.getCell(r, c).setRemoved(removedCells[r][c]);
        b.getCell(r, c).setPlayer(playerCells[r][c]);
      }
    }
  }
}
