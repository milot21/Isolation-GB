package main.gameobjects;

/**
 * Move becomes a record class since it's a data carrier
 * @param newCol/newRow   next location
 * @param tokenCol/tokenROw token were removing
 */
public record Move(int newRow, int newCol, int tokenRow, int tokenCol) {

  @Override
  public String toString() {
    return "Move to (" + newRow + "," + newCol + "), remove (" + tokenRow + "," + tokenCol + ")";
  }

}
