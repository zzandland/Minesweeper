/**
 * Board is where Grid instances are instantiated and stored into a 2D matrix and provides all
 * related functionalities, such as priting the game board, randomizing mines, and validating a
 * coordinate is within the board's range.
 *
 * @author Si Yong Kim
 * @version 1.0
 * @since 2019-03-29
 */
public class Board {
  /** Number of mines in the board */
  private int nMines;
  /** Height of the board */
  private int y;
  /** Width of the board */
  private int x;
  /** The actual board containing Grid instances */
  private Grid[][] board;
  /** Number of grid remaining to be checked */
  private int gridToCheck;

  /**
   * Initialize the game board with given height and width, and number of mines of the board, which
   * is used to measure number of mines to plant.
   *
   * @param y the height of the board
   * @param x the width of the board
   * @param nMines number of mines to be planted
   */
  public Board(int y, int x, int nMines) {
    this.y = y;
    this.x = x;
    this.nMines = nMines;
    board = new Grid[y][x];
    gridToCheck = y * x - nMines;
  }

  /**
   * Get the game {@link #board}.
   *
   * @return game board
   */
  public Grid[][] getBoard() {
    return board;
  }

  /**
   * Get the value of {@link #gridToCheck}.
   *
   * @return number of remaining grids to check
   */
  public int getGridToCheck() {
    return gridToCheck;
  }

  /** When a grid is successfully checked, decrement the {@link #gridToCheck} attribute. */
  public void decrementGridToCheck() {
    gridToCheck--;
  }

  /**
   * Populate each coordinate of the game {@link #board} with {@link Grid} instances. Then, the
   * board is iterated to plant mine with a random probabilty until the number of mines planted
   * equals the value of data field {@link #nMines}. Then, each grid is iterated to calculate number
   * of adjacent mines.
   *
   * @see #randomizeMines()
   * @see Grid#Grid(int, int)
   * @see Grid#countAdjBomb(Board)
   */
  public void initBoard() {
    for (int i = 0; i < y; i++) {
      for (int j = 0; j < x; j++) {
        board[i][j] = new Grid(i, j);
      }
    }

    randomizeMines();

    for (int i = 0; i < y; i++) {
      for (int j = 0; j < x; j++) {
        board[i][j].countAdjBomb(this);
      }
    }
  }

  /**
   * Populate the game {@link #board} with mines as specified in the data field {@link #nMines}. The
   * method ensures that grids with mines are as separated as possible from one another so that the
   * mine pattern does not form a few patches. Until the number of Mines planted equals the nOfMines
   * attribute value, the board is iterated repeatedly.
   *
   * @see Grid#isMine()
   * @see Grid#plantMine()
   */
  private void randomizeMines() {
    int nMinePlanted = 0;
    double threshold = 1 - 1.0 / (y * x);
    while (nMinePlanted < nMines) {
      for (int i = 0; i < y; i++) {
        for (int j = 0; j < x; j++) {
          double random = Math.random();
          Grid grid = board[i][j];
          if (random > threshold && !grid.isMine()) {
            grid.plantMine();
            if (++nMinePlanted >= nMines) return;
          }
        }
      }
    }
  }

  /**
   * Print the {@link #board} to the user. Checked coordinates will show number of adjacent mines.
   * Unchecked coordinates will show as a empty space. A coordinate marked by the user as a mine
   * will show as '*', and a coordinate marked as question mark (user is uncertain of the identity)
   * shows as '?'.
   *
   * @param showAnswer if game is over, the board printed will show all grids that contain mines
   * @see Grid#isMine()
   * @see Grid#getNAdjMine()
   */
  public void printBoard(boolean showAnswer) {
    StringBuilder rowBuild = new StringBuilder("   ");
    for (int i = 0; i < x; i++) {
      int tenth = (i + 1) / 10;
      if (tenth > 0) rowBuild.append(String.format(" %d ", tenth));
      else rowBuild.append("   ");
    }
    System.out.println(rowBuild.toString());

    rowBuild = new StringBuilder("   ");
    for (int i = 0; i < x; i++) {
      rowBuild.append(String.format(" %d ", (i + 1) % 10));
    }
    System.out.println(rowBuild.toString() + '\n');

    for (int i = 0; i < y; i++) {
      rowBuild = new StringBuilder();
      if (i < 9) {
        rowBuild.append(String.format(" %d ", i + 1));
      } else {
        rowBuild.append(String.format("%d ", i + 1));
      }
      for (int j = 0; j < x; j++) {
        Grid grid = board[i][j];
        if (grid.isMarkedMine()) { 
          rowBuild.append(" * ");
        } else if (grid.isMarkedQuestion()) { 
          rowBuild.append(" ? ");
        } else if (grid.isChecked()) {
          int n = grid.getNAdjMine();
          if (n == 0) rowBuild.append("   ");
          else rowBuild.append(String.format(" %d ", grid.getNAdjMine()));
        } else {
          if (showAnswer) {
            if (grid.isMine()) rowBuild.append(" * ");
            else rowBuild.append(" X ");
          } else {
            rowBuild.append(" X ");
          }
        }
      }
      System.out.println(rowBuild.toString());
    }
  }

  /**
   * Validate if the given coordinate is within the game {@link #board}. The given coordinate's y
   * and x indexes must be greater or equal to 0, and smaller than the width {@link #x} and height
   * {@link #y} of the game board.
   *
   * @param coord the coordinate to validate
   * @return <code>true</code> if the given coordinate is valid; 
   *         <code>false</code> if the given
   *         coordinate is invalid
   */
  public boolean validateCoord(int[] coord) {
    return (coord[0] >= 0 && coord[0] < y) && (coord[1] >= 0 && coord[1] < x);
  }

  public static void main(String[] args) {
    Board board = new Board(30, 20, 100);
    board.initBoard();
    board.printBoard(true);
  }
}
