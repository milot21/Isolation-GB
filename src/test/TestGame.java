package test;
import main.*;

public class TestGame {
  public static void main(String[] args) {
    Player p = new Player(0,2,"Me");
    System.out.println(p);
    p.setRow(1);
    p.setCol(4);
    System.out.println(p);
  }
}

