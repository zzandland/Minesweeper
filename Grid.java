public class Grid {
  private int[] coord;
  private boolean checked;
  private boolean mine;
  private boolean markedMine;
  private boolean markedQuestion;
  private int nAdjMine;

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
   * @return <code>true</code> if the grid is checked <code>false</code> if the grid has not been
   *     checked
   */
  public boolean isChecked() {
    return checked;
  }

  /**
   * Check if the grid contains a mine.
   *
   * @return <code>true</code> if the grid contains a mine <code>false</code> if the grid does not
   *     contain a mine
   */
  public boolean isMine() {
    return mine;
  }

  /**
   * Check if the grid is marked by the user as a mine field.
   *
   * @return <code>true</code> if the grid is marked as a mine field <code>false</code> if the grid
   *     is not marked as a mine field
   */
  public boolean isMarkedMine() {
    return markedMine;
  }

  /**
   * Check if the grid is marked by the user as a question field.
   *
   * @return <code>true</code> if the grid is marked as a question field <code>false</code> if the
   *     grid is not marked as a question field
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
   * Count number of grids with mines within the 8 adjacent grids.
   *
   * @param board the game board
   * @return number of mines in the adjacent grids.
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
   * @param degree decimal degree representing direction to check
   * @param board the game board
   * @return <code>1</code> if the adjacent grid contains a mine <code>0</code> if the adjacent grid
   *     does not contain a mine
   */
  private int countAdjBombHelper(int degree, Board board) {
    int[] target = getAdjCoord(degree);
    if (!board.validateCoord(target)) return 0;
    Grid targetGrid = board.getBoard()[target[0]][target[1]];
    return (targetGrid.mine) ? 1 : 0;
  }

  /**
   * Check the grid. If the grid contains a mine, the game is over. If the grid does not contain any neighboring grids with mines, all of the eight adjacent grids are automatically checked as well. The process recursively repeats until there is no more grids whose neighboring grids do not contain mines. The method is implemented using a depth-first search approach.
   *
   * @return <code>true</code> the grid does not contain a mine and is safely checked <code>false
   *     </code> the grid contains a mine and now the game is over
   */
  public boolean checkGrid(Board board) {
    if (mine) return false;

    checked = true;
    markedMine = false;
    markedQuestion = false;
    board.decrementGridToCheck();
    if (nAdjMine == 0) {
      checkGridHelper(0, board);
      checkGridHelper(45, board);
      checkGridHelper(90, board);
      checkGridHelper(135, board);
      checkGridHelper(180, board);
      checkGridHelper(225, board);
      checkGridHelper(270, board);
      checkGridHelper(315, board);
      checkGridHelper(360, board);
    }
    return true;
  }

  /**
   * Check the adjacent grid in the direction represented as the decimal degree. If the adjacent grid in the direction is already checked, then it is not visited. This ensures that endless recursive calls do not occur.
   *
   * @param degree decimal degree representing direction of the neighboring grid
   * @param board the game board
   */
  private void checkGridHelper(int degree, Board board) {
    int[] target = getAdjCoord(degree);
    if (!board.validateCoord(target)) return;
    Grid targetGrid = board.getBoard()[target[0]][target[1]];
    if (!targetGrid.checked) targetGrid.checkGrid(board);
  }

  /**
   * Find the y/x coordinate of an adjacent grid. The direction is represented in decimal degree system, where on full cycle is 360 degrees.
   *
   * @param degree decimal degree representing direction of the neighboring grid
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
