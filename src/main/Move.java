package main;

public class Move {
  private int newRow, newCol;  //next location
  private int tokenRow, tokenCol; //token were removing

  public Move(int newRow, int newCol, int tokenRow, int tokenCol) {
    this.newRow = newRow;
    this.newCol = newCol;
    this.tokenRow = tokenRow;
    this.tokenCol = tokenCol;
  }
  public int getNewRow() {return newRow;}
  public int getNewCol() {return newCol;}
  public int getTokenRow() {return tokenRow;}
  public int getTokenCol() {return tokenCol;}

  @Override
  public String toString() {
    return "main.Move to ("+newRow+","+newCol+"), remove ("+tokenRow+","+tokenCol+")";
  }

}
