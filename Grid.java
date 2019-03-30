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
   * @return  <code>true</code> if the grid is checked
   *          <code>false</code> if the grid has not been checked
   */
  public boolean isChecked() {
    return checked;
  }

  /**
   * Check if the grid contains a mine.
   *
   * @return  <code>true</code> if the grid contains a mine
   *          <code>false</code> if the grid does not contain a mine
   */
  public boolean isMine() {
    return mine;
  }

  /**
   * Check if the grid is marked by the user as a mine field.
   *
   * @return  <code>true</code> if the grid is marked as a mine field
   *          <code>false</code> if the grid is not marked as a mine field
   */
  public boolean isMarkedMine() {
    return markedMine;
  }

  /**
   * Check if the grid is marked by the user as a question field.
   *
   * @return  <code>true</code> if the grid is marked as a question field 
   *          <code>false</code> if the grid is not marked as a question field
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

  /**
   * Plant a mine to the grid.
   */
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
   * @return  <code>1</code> if the adjacent grid contains a mine
   *          <code>0</code> if the adjacent grid does not contain a mine
   */
  private int countAdjBombHelper(int degree, Board board) {
    int[] target;
    switch (degree) {
      case 0:
        target = new int[] {coord[0] - 1, coord[1]};
        break;
      case 45:
        target = new int[] {coord[0] - 1, coord[1] + 1};
        break;
      case 90:
        target = new int[] {coord[0], coord[1] + 1};
        break;
      case 135:
        target = new int[] {coord[0] + 1, coord[1] + 1};
        break;
      case 180:
        target = new int[] {coord[0] + 1, coord[1]};
        break;
      case 225:
        target = new int[] {coord[0] + 1, coord[1] - 1};
        break;
      case 270:
        target = new int[] {coord[0], coord[1] - 1};
        break;
      case 315:
        target = new int[] {coord[0] - 1, coord[1] - 1};
        break;
      default:
        target = coord;
    }
    if (!board.validateCoord(target)) return 0;
    Grid targetGrid = board.getBoard()[target[0]][target[1]];
    return (targetGrid.mine) ? 1 : 0;
  }
}
