package test;

import main.gameobjects.GameBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

  @Test
  public void testInitMovesForP1() {
    GameBoard b = new GameBoard();
    assertFalse(b.getLegalMoves(b.getP1()).isEmpty(), "P1 should have legal moves at the start");

  }
}
