package main.gameobjects;

public class Player {
  private int row,col;
  private final String id;


  /**
   * @param row row
   * @param col col
   * @param id identification
   */
  public Player(int row, int col, String id) {
    this.row = row;
    this.col = col;
    this.id = id;
  }


  public int getRow(){return row;}    // Getter for the row were in
  public int getCol(){return col;}// Getter for the column

  public void setRow(int row){this.row = row;}
  public void setCol(int col){this.col = col;}

  public String getId(){return id;}   // Getter for the players ID

  @Override
  public String toString(){
    return id + " (" + row + "," + col + ")";
  }
}
