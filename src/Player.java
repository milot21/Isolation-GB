public class Player {
  private int row,col;
  private String id;


  /**
   * @param row
   * @param col
   * @param id
   */
  public Player(int row, int col, String id) {
    this.row = row;
    this.col = col;
    this.id = id;
  }


  public int getRow(){return row;}    // Getter for the row were in
  public int getCol(){return col;}    // Getter for the column
  public String getId(){return id;}   // Getter for the players ID
}
