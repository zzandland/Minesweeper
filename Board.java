public class Board {
  private int nMines;
  private int y;
  private int x;
  private Grid[][] board;

  public Board(int y, int x, int nMines) {
    this.y = y;
    this.x = x;
    this.nMines = nMines;
    board = new Grid[y][x];
  }

  /**
   * Get the game board.
   *
   * @return Grid[][] game board
   */
  public Grid[][] getBoard() {
    return board;
  }

  /**
   * Initialize the game board with given height and width and randomized mines. First, the game
   * board is populated with Grid instances of two kinds: grids with mines and grids without. Then,
   * each grid is iterated to calculate number of adjacent mines.
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
   * Populate the game board with mines as specified in the data field nOfMines. The method ensures
   * that grids with mines are as separated as possible from one another so that the mine pattern
   * does not form a few patches. Until the number of Mines planted equals the nOfMines attribute
   * value, the board is iterated repeatedly.
   */
  private void randomizeMines() {
    int nMinePlanted = 0;
    double threshold = 1 - 1.0 / (y * x);
    while (nMinePlanted < nMines) {
      for (int i = 0; i < y; i++) {
        for (int j = 0; j < x; j++) {
          double random = Math.random();
          Grid grid = board[i][j];
          if (random > threshold && !grid.getIsMine()) {
            grid.plantMine();
            if (++nMinePlanted >= nMines) return;
          }
        }
      }
    }
  }

  /**
   * Print the board to the user. Checked coordinates will show number of adjacent mines. Unchecked
   * coordinates will show as a empty space. A coordinate marked by the user as a mine will show as
   * '*', and a coordinate marked as question mark (user is uncertain of the identity) shows as '?'.
   */
  public void printBoard() {
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
        if (grid.getIsMine()) rowBuild.append(" * ");
        else rowBuild.append("   ");
      }
      System.out.println(rowBuild.toString());
    }
  }

  /**
   * Validate if the given coordinate is within the game board. The given coordinate's y and x
   * indexes must be greater or equal to 0, and smaller than the width (x) and height (y) of the
   * game board.
   *
   * @param int[] coord the coordinate to validate
   * @return boolean value notating if the given coordinate is valid.
   */
  public boolean validateCoord(int[] coord) {
    return (coord[0] >= 0 && coord[0] < y) && (coord[1] >= 0 && coord[1] < x);
  }

  public static void main(String[] args) {
    Board board = new Board(30, 20, 100);
    board.initBoard();
    board.printBoard();
  }
}
