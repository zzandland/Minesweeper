/**
 * Check if one of the eight adjacent grids contains a mine. The coordinates are first validated
 * Grid provides core functionality in the Minesweeper game related to each grid in the game
 * board, such as saving information such as if the grid contains a mine, if the grid is already 
 * checked by the user , if the grid is marked as either mine or question field by the user as 
 * an user-friendly feature, and number of adjacent mine fields to print after the field is 
 * checked. In addition, it contains methods that implement how grid checking occurs recursively
 * until the boundary is made of checked grids with adjacent mine fields, which is implemented 
 * using depth-first search approach.
 *
 * @author Si Yong Kim
 * @version 1.0
 * @since 2019-03-29
 */
public class Grid {
  /** Coordinate of where the Grid is located in the game board */
  private int[] coord;
  /** The grid is checked by the user */
  private boolean checked;
  /** The grid contains a mine */
  private boolean mine;
  /** The grid is marked as a mine field */
  private boolean markedMine;
  /** The grid is marked as a question field */
  private boolean markedQuestion;
 /** Number of neighborin grids that contain mines */
  private int nAdjMine;

  /**
   * Initialize a Grid with the given y and x coordinates.
   *
   * @param y the y coordinate of the Grid instance in the game board
   * @param x the x coordinate of the Grid instance in the game board
   */
  public Grid(int y, int x) {
    coord = new int[] {y, x};
    checked = false;
    mine = false;
    markedMine = false;
    markedQuestion = false;
  }

  /**
   * Check if the grid is checked.
   *
   * @return <code>true</code> if the grid is checked;
   *         <code>false</code> if the grid has not been checked
   */
  public boolean isChecked() {
    return checked;
  }

  /**
   * Check if the grid contains a mine.
   *
   * @return <code>true</code> if the grid contains a mine;
   *         <code>false</code> if the grid does not contain a mine
   */
  public boolean isMine() {
    return mine;
  }

  /**
   * Check if the grid is marked by the user as a mine field.
   *
   * @return <code>true</code> if the grid is marked as a mine field; 
   *         <code>false</code> if the grid is not marked as a mine field
   */
  public boolean isMarkedMine() {
    return markedMine;
  }

  /**
   * Check if the grid is marked by the user as a question field.
   *
   * @return <code>true</code> if the grid is marked as a question field;
   *         <code>false</code> if the grid is not marked as a question field
   */
  public boolean isMarkedQuestion() {
    return markedQuestion;
  }

  /**
   * Get number of adjacent grids with mines.
   *
   * @return number of adjacent grids with mines
   */
  public int getNAdjMine() {
    return nAdjMine;
  }

  /** Plant a mine to the grid. */
  public void plantMine() {
    mine = true;
  }

  /**
   * Count number of grids with mines within the 8 adjacent grids and assign it to {@link #nAdjMine}.
   *
   * @param board the game board
   * @see #countAdjBombHelper(int, Board)
   */
  public void countAdjBomb(Board board) {
    this.nAdjMine =
        countAdjBombHelper(0, board)
            + countAdjBombHelper(45, board)
            + countAdjBombHelper(90, board)
            + countAdjBombHelper(135, board)
            + countAdjBombHelper(180, board)
            + countAdjBombHelper(225, board)
            + countAdjBombHelper(270, board)
            + countAdjBombHelper(315, board);
  }

  /**
   * Check if one of the eight adjacent grids contains a mine. The coordinates are first validated
   * if they are within the valid boundary of the game board. The direction is represented in
   * decimal degree system, where one full cycle is 360 degrees. This method returns 1 if the grid
   * contains a mine, or 0 if not.
   *
   * @param degree decimal {@link #degree} representing direction to check
   * @param board the game {@link Board#board}
   * @return <code>1</code> if the adjacent grid contains a mine;
   *         <code>0</code> if the adjacent grid does not contain a mine
   * @see #getAdjCoord(int)
   * @see Board#getBoard()
   * @see Board#validateCoord(Grid)
   */
  private int countAdjBombHelper(int degree, Board board) {
    int[] target = getAdjCoord(degree);
    if (!board.validateCoord(target)) return 0;
    Grid targetGrid = board.getBoard()[target[0]][target[1]];
    return (targetGrid.mine) ? 1 : 0;
  }

  /**
   * Check the grid. If the grid contains a mine, the game is over. If the grid does not contain any
   * neighboring grids with mines, all of the eight adjacent grids are automatically checked as
   * well. The process recursively repeats until a new boundary is made of checked grids that
   * contain adjacent grids with mines. The method is implemented using a depth-first search
   * approach.
   *
   * @param board the game {@link Board#board}
   * @return <code>true</code> the grid does not contain a mine and is safely checked;
   *         <code>false </code> the grid contains a mine and now the game is over
   * @see #checkGridHelper(Board)
   */
  public boolean checkGrid(Board board) {
    if (mine) return false;

    checked = true;
    markedMine = false;
    markedQuestion = false;
    board.decrementGridToCheck();

    if (nAdjMine == 0) {
      checkGridHelper(board);
    }
    return true;
  }

  /**
   * If the checked grid contains no neighboring grids with mines, then recursively check all of
   * the eight adjacent grids.
   *
   * @param board the game {@link Board#board}
   */
  private void checkGridHelper(Board board) {
    checkGridDirectionHelper(0, board);
    checkGridDirectionHelper(45, board);
    checkGridDirectionHelper(90, board);
    checkGridDirectionHelper(135, board);
    checkGridDirectionHelper(180, board);
    checkGridDirectionHelper(225, board);
    checkGridDirectionHelper(270, board);
    checkGridDirectionHelper(315, board);
  }

  /**
   * Check the adjacent grid in the direction represented as the decimal degree. If the adjacent
   * grid in the direction is already checked or contains a mine, then it is not visited. This
   * ensures that endless recursive calls do not occur.
   *
   * @param degree decimal {@link #degree} representing direction of the neighboring grid
   * @param board the game {@link Board#board}
   * @see #getAdjCoord(int)
   * @see Board#validateCoord(Grid)
   */
  private void checkGridDirectionHelper(int degree, Board board) {
    int[] target = getAdjCoord(degree);
    if (!board.validateCoord(target)) return;
    Grid targetGrid = board.getBoard()[target[0]][target[1]];
    if (!targetGrid.mine && !targetGrid.checked) {
      targetGrid.checkGrid(board);
    }
  }

  /**
   * Find the y/x coordinate of an adjacent grid. The direction is represented in decimal degree
   * system, where on full cycle is 360 degrees.
   *
   * @param degree decimal {@link #degree} representing direction of the neighboring grid
   * @return the coordinate of the adjacent grid in the board
   */
  private int[] getAdjCoord(int degree) {
    int[] output;
    switch (degree) {
      case 0:
        output = new int[] {coord[0] - 1, coord[1]};
        break;
      case 45:
        output = new int[] {coord[0] - 1, coord[1] + 1};
        break;
      case 90:
        output = new int[] {coord[0], coord[1] + 1};
        break;
      case 135:
        output = new int[] {coord[0] + 1, coord[1] + 1};
        break;
      case 180:
        output = new int[] {coord[0] + 1, coord[1]};
        break;
      case 225:
        output = new int[] {coord[0] + 1, coord[1] - 1};
        break;
      case 270:
        output = new int[] {coord[0], coord[1] - 1};
        break;
      case 315:
        output = new int[] {coord[0] - 1, coord[1] - 1};
        break;
      default:
        output = coord;
    }
    return output;
  }

  /**
   * Mark the grid as containing a mine. If the grid is already marked as a question field, then
   * markedQuestion attribute is assigned to false.
   */
  public void markAsMine() {
    markedMine = true;
    markedQuestion = false;
  }

  /**
   * Mark the grid as an uncertain identity. If the grid is already marked as a mine field, then
   * markedMine attribute is assigned to false.
   */
  public void markAsQuestion() {
    markedMine = false;
    markedQuestion = true;
  }

  /** Unmark the grid from either mine or question field. */
  public void unmark() {
    markedMine = false;
    markedQuestion = false;
  }
}
