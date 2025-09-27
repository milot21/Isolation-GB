package main;

public class Cell {
  final private int row, col;
  private Player player;
  private boolean removed;

  /**
   * Holds the cells information, like location, or
   * if it has it's token available
   * @param row
   * @param col
   */
  public Cell(int row, int col) {
    this.row = row;
    this.col = col;
    this.removed = false;
  }

  public Player getPlayer() {return player;}  // Getter for the main.Player instance obj

  public void setPlayer(Player p) {this.player = p;} // Setter for the player


  public void removeToken() {this.removed = true;}  //removes Token in cell and sets it to true
  public boolean isRemoved() {return removed;}     //checks if that cell has it's token removed
}
