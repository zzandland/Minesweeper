import java.io.IOException;

/**
 * <h1>Minesweeper</h1>
 *
 * <p>This is a text-based implementation of the classic Minesweeper game, written in Java
 * programming language. This program is written as the final project for UC Berkely Extension
 * course ELENGX436.2-014, taught by Carl Limsico.
 *
 * @author Si Yong Kim
 * @version 1.0
 * @since 2019-03-29
 */
public class Main {
  public static void main(String[] args) {
    Game game = new Game();
    try {
      game.initGame();
    } catch (IOException ioe) {
      System.err.println("IO exception: " + ioe);
    }
  }
}
