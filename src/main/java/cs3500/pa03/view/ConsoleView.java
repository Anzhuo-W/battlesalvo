package cs3500.pa03.view;

import cs3500.pa03.model.BoardState;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console based viewer
 */
public class ConsoleView implements View {
  private final BoardState user;
  private final BoardState opponent;
  private final Readable input;
  private final Appendable outputTo;
  private final Scanner scanner;

  /**
   * Constructor for the ConsoleView
   *
   * @param userBoard the board of the manual player
   * @param opponentBoard the board of the opponent AI player
   * @param i readable input to read from
   * @param o appendable output to return to
   */
  public ConsoleView(BoardState userBoard, BoardState opponentBoard, Readable i, Appendable o) {
    user = userBoard;
    opponent = opponentBoard;
    input = i;
    outputTo = o;
    scanner = new Scanner(input);
  }

  /**
   * Displays strings to the console
   *
   * @param toDisplay string to display
   */
  @Override
  public void displayContent(String toDisplay) {
    try {
      outputTo.append(toDisplay);
    } catch (IOException e) {
      throw new RuntimeException("Cannot display to output");
    }
  }

  /**
   * Displays string to console and gets user response
   *
   * @param toDisplay message to output to view
   * @return user input as string
   */
  @Override
  public List<String> displayPrompt(String toDisplay, int numExpected) {
    ArrayList<String> userInput = new ArrayList<>();
    try {
      outputTo.append(toDisplay);
    } catch (IOException e) {
      throw new RuntimeException("Cannot display to output");
    }
    String next;
    for (int i = 0; i < numExpected; i++) {
      if (scanner.hasNext()) {
        next = scanner.next();
        userInput.add(next);
      }
    }
    return userInput;
  }

  /**
   * Displays the board to appendable
   *
   * @param player player to display, either user or opponent
   */
  public void displayBoard(String player) {
    ArrayList<ArrayList<String>> toDisplay;
    if (player.equals("user")) {
      toDisplay = user.getBoard();
    } else if (player.equals("opponent")) {
      toDisplay = opponent.getBoard();
    } else {
      throw new IllegalArgumentException("invalid input for displaying board");
    }

    StringBuilder formattedBoard = new StringBuilder();
    for (ArrayList<String> row : toDisplay) {
      for (String col : row) {
        formattedBoard.append(col);
        formattedBoard.append(" ");
      }
      formattedBoard.replace(formattedBoard.length() - 1, formattedBoard.length(), "\n");
    }
    try {
      outputTo.append(formattedBoard.toString());
    } catch (IOException e) {
      throw new RuntimeException("Cannot display to output");
    }
  }
}
