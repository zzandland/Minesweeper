import java.io.BufferedReader;
import java.io.IOException;

public class Game {
  private boolean gameOver;

  public Game() {
    gameOver = false;
  }

  public void initGame() throws IOException {
    BufferedReader userInputReader = Input.generateBR(System.in);
    System.out.print("Enter the board's height: ");
    int height = Integer.parseInt(userInputReader.readLine());
    System.out.print("Enter the board's width: ");
    int width = Integer.parseInt(userInputReader.readLine());
    System.out.print("Enter the number of mines: ");
    int mines = Integer.parseInt(userInputReader.readLine());
    Board board = new Board(height, width, mines);
    board.initBoard();

    while (!gameOver) takeTurn(board);

    userInputReader.close();
  }

  private void takeTurn(Board board) throws IOException {
    BufferedReader userInputReader = Input.generateBR(System.in);
    System.out.println(
        "Select a grid to perform a further action. Enter the y and x coordinates with a empty character in between. For example, to select the grid with y coordinate of 3 and x coordinate of 8, enter \"3 8\"");
    String[] selectedCoord = userInputReader.readLine().split(" ");
    Grid selectedGrid =
        board.getBoard()[Integer.parseInt(selectedCoord[0])][Integer.parseInt(selectedCoord[1])];
    performOperationOnGrid(selectedGrid);
  }

  private void performOperationOnGrid(Grid grid) throws IOException {
    if (grid.isChecked()) {
      System.out.println("The grid is already checked. Select another grid.");
      return;
    }

    BufferedReader userInputReader = Input.generateBR(System.in);
    System.out.println(generateOperationMenu(grid));
    int option = Integer.parseInt(userInputReader.readLine());

    switch (option) {
      case 1:
        if (grid.checkTheGrid()) {
        
        } else {
          System.out.println("The grid contains a mine! Game over");
          gameOver = true;
          return;
        }
    }
  }

  private String generateOperationMenu(Grid grid) {
    StringBuilder build = new StringBuilder();
    build.append("Enter the desired operation.");
    build.append("\n1. Check the grid (Gameover if there is a mine!!)");
    if (!grid.isMarkedMine() && !grid.isMarkedQuestion()) {
      build.append("\n2. Mark the grid as a mine field");
      build.append("\n3. Mark the grid as a question field");
    } else {
      if (grid.isMarkedMine()) build.append("\n2. Change the grid to a question field");
      else if (grid.isMarkedQuestion()) build.append("\n2. Change the grid to a mine field");
      build.append("\n4. Unmark the grid");
    }
    return build.toString();
  }
}
