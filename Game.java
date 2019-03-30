import java.io.BufferedReader;
import java.io.IOException;

/**
 * Game contains core functions related to how game logics and mechanics work, and user interface
 * implementations so that the Minesweeper game can be played in text-based fashion. The game
 * continues as long as the attribute gameOver is assigned to false.
 *
 * @author Si Yong Kim
 * @version 1.0
 * @since 2019-03-29
 */
public class Game {
  /** Determines if the game is over */
  private boolean gameOver;
  /** Number of remaining mines as marked by the user. May be different from the true number */
  private int nLeftMine;

  /** Initialize a new game with the gameOver attribute assigned as false. */
  public Game() {
    gameOver = false;
  }

  /**
   * Initialize the game by asking the user inputs for height, width, and number of mines in the
   * game board. Then, the method instructs the user on how to enter the user input for selecting a
   * coordinate. The method {@link #takeTurn(Board)} is continually invoked until the game is over.
   * When game is over, at the end prints the all of the locations of the grids containing mines.
   *
   * @throws IOException On input error
   * @see #takeTurn(Board)
   * @see Board#initBoard()
   * @see Board#printBoard(boolean)
   */
  public void initGame() throws IOException {
    BufferedReader userInputReader = Input.generateBR(System.in);
    System.out.print("Enter the board's height: ");
    int height = Integer.parseInt(userInputReader.readLine().trim());
    System.out.print("Enter the board's width: ");
    int width = Integer.parseInt(userInputReader.readLine().trim());
    System.out.print("Enter the number of mines: ");
    int mines = Integer.parseInt(userInputReader.readLine().trim());
    Board board = new Board(height, width, mines);
    board.initBoard();
    nLeftMine = mines;

    String instruction =
        "Select a grid to perform a further action."
            + "\nEnter the y and x coordinates with a empty character in between. "
            + "\nFor example, to select the grid with y coordinate of 3 and x coordinate of 8, enter \"3 8\"";
    System.out.println(instruction);

    while (!gameOver) takeTurn(board);

    board.printBoard(true);
    userInputReader.close();
  }

  /**
   * First, if number of grids remaining to check becomes 0, the game is won. Otherwise, the user is
   * asked to enter coordinates on where to select. The method {@link #gridOption(Grid, Board)} is
   * invoked on the selected coordinate. If the user input is out of the board boundary, the {@link
   * ArrayIndexOutOfBoundsException} exception is thrown. If the user inputs nothing, the {@link
   * NumberFormatException} exception is thrown.
   *
   * @param board the game {@link Board}
   * @throws ArrayIndexOutOfBoundsException On incorrect coordinate inputs
   * @throws IOException On input error
   * @see Board#getGridToCheck()
   * @see Board#printBoard(boolean)
   * @see Input#generateBR(StreamInput)
   */
  private void takeTurn(Board board) throws IOException {
    try {
      if (board.getGridToCheck() == 0) {
        System.out.println("You've checked all of the grids! Game won");
        gameOver = true;
        return;
      }

      board.printBoard(false);
      System.out.println("Number of mines left: " + nLeftMine);

      BufferedReader userInputReader = Input.generateBR(System.in);
      System.out.println("Enter the coordinate as instructed to select a grid:");
      String[] selectedCoord = userInputReader.readLine().trim().split(" ");
      int yCoord = Integer.parseInt(selectedCoord[0]) - 1;
      int xCoord = Integer.parseInt(selectedCoord[1]) - 1;
      Grid selectedGrid = board.getBoard()[yCoord][xCoord];
      gridOption(selectedGrid, board);
    } catch (ArrayIndexOutOfBoundsException aioobe) {
      System.out.println("The given coordinate is invalid. Please try again.");
      takeTurn(board);
    } catch (NumberFormatException nfe) {
      System.out.println("The given coordinate is invalid. Please try again.");
      takeTurn(board);
    }
  }

  /**
   * If the grid is already checked, the method is immediately returned. Otherwise, based on the
   * current state of the grid, one of three different grid option methods is invoked.
   *
   * @param grid the current {@link Grid}
   * @param board the game {@link Board}
   * @throws IOException On input error
   * @see #markedMineGridOption(Grid, Board)
   * @see #markedQuestionGridOption(Grid, Board)
   * @see #unmarkedGridOption(Grid, Board)
   * @see Grid#isChecked()
   * @see Grid#isMarkedMine()
   * @see Grid#isMarkedQuestion()
   */
  private void gridOption(Grid grid, Board board) throws IOException {
    if (grid.isChecked()) {
      System.out.println("The grid is already checked. Select another grid.");
      return;
    }

    if (!grid.isMarkedMine() && !grid.isMarkedQuestion()) {
      unmarkedGridOption(grid, board);
    } else {
      if (grid.isMarkedMine()) markedMineGridOption(grid, board);
      else if (grid.isMarkedQuestion()) markedQuestionGridOption(grid, board);
    }
  }

  /**
   * If the grid is not marked as either a mine or a question field, then choices to mark the grid
   * become available.
   *
   * @param grid the current {@link Grid}
   * @param board the game {@link Board}
   * @throws IOException On input error
   * @see #generateOperationMenu(Grid)
   * @see Grid#checkGrid(Board)
   * @see Grid#markAsMine()
   * @see Grid#markAsQuestion()
   * @see Input#generateBR(InputStream)
   */
  private void unmarkedGridOption(Grid grid, Board board) throws IOException {
    BufferedReader userInputReader = Input.generateBR(System.in);
    System.out.println(generateOperationMenu(grid));
    int option = Integer.parseInt(userInputReader.readLine());

    switch (option) {
      case 1:
        if (!grid.checkGrid(board)) {
          System.out.println("The grid contains a mine! Game over");
          gameOver = true;
          return;
        }
        break;
      case 2:
        if (nLeftMine == 0) {
          System.out.println(
              "All mines are already marked: some of the marked gris must be not mine fields.");
          break;
        }
        grid.markAsMine();
        nLeftMine--;
        break;
      case 3:
        grid.markAsQuestion();
        break;
      default:
        System.out.println("Invalid choice. Please select again.");
        gridOption(grid, board);
    }
  }

  /**
   * If the grid is marked as a mine field, then the choice to check the grid is not available.
   * Instead, choices to change the marking to a question field and unmark the grid are added.
   *
   * @param grid the current {@link Grid}
   * @param board the game {@link Board}
   * @throws IOException On input error
   * @see #generateOperationMenu(Grid)
   * @see Grid#checkGrid(Board)
   * @see Grid#markAsQuestion()
   * @see Grid#unmark()
   * @see Input#generateBR(InputStream)
   */
  private void markedMineGridOption(Grid grid, Board board) throws IOException {
    BufferedReader userInputReader = Input.generateBR(System.in);
    System.out.println(generateOperationMenu(grid));
    int option = Integer.parseInt(userInputReader.readLine());

    switch (option) {
      case 1:
        grid.markAsQuestion();
        nLeftMine++;
        break;
      case 2:
        grid.unmark();
        nLeftMine++;
        break;
      default:
        System.out.println("Invalid choice. Please select again.");
        gridOption(grid, board);
    }
  }

  /**
   * If the grid is marked as a question field, then the choice to check the grid is not available.
   * Instead, choices to change the marking to a mine field and unmark the grid are added.
   *
   * @param grid the current {@link Grid}
   * @param board the game {@link Board}
   * @throws IOException On input error
   * @see #generateOperationMenu(Grid)
   * @see Grid#checkGrid(Board)
   * @see Grid#markAsMine()
   * @see Grid#unmark()
   * @see Input#generateBR(InputStream)
   */
  private void markedQuestionGridOption(Grid grid, Board board) throws IOException {
    BufferedReader userInputReader = Input.generateBR(System.in);
    System.out.println(generateOperationMenu(grid));
    int option = Integer.parseInt(userInputReader.readLine());

    switch (option) {
      case 1:
        grid.markAsMine();
        break;
      case 2:
        grid.unmark();
        break;
      default:
        System.out.println("Invalid choice. Please select again.");
        gridOption(grid, board);
    }
  }

  /**
   * Generate the available options based on the state of the grid.
   *
   * @param grid the current {@link Grid}
   * @return the text containing the option listing
   * @see Grid#isMarkedMine()
   * @see Grid#isMarkedQuestion()
   */
  private String generateOperationMenu(Grid grid) {
    StringBuilder build = new StringBuilder();
    build.append("Enter the desired operation.");
    if (!grid.isMarkedMine() && !grid.isMarkedQuestion()) {
      build.append("\n1. Check the grid (Gameover if there is a mine!!)");
      build.append("\n2. Mark the grid as a mine field");
      build.append("\n3. Mark the grid as a question field");
    } else {
      if (grid.isMarkedMine()) build.append("\n1. Change the grid to a question field");
      else if (grid.isMarkedQuestion()) build.append("\n2. Change the grid to a mine field");
      build.append("\n2. Unmark the grid");
    }
    return build.toString();
  }

  public static void main(String[] args) {
    Game game = new Game();
    try {
      game.initGame();
    } catch (IOException ioe) {
      System.err.println("IO exception: " + ioe);
    }
  }
}
