package cs3500.pa03.view;

import java.util.List;

/**
 * Viewer interface
 */
public interface View {
  /**
   * Prints a string to the display
   *
   * @param toDisplay string to display
   */
  void displayContent(String toDisplay);

  /**
   * Displays a string prompt and gets a user response
   *
   * @param toDisplay message to output to view
   * @param numExpected number of tokens to take
   * @return List of strings of user inputs, separated into the respective tokens
   */
  List<String> displayPrompt(String toDisplay, int numExpected);

  /**
   * Displays the specified player's board to appendable output
   *
   * @param player player to display, either user or opponent
   */
  void displayBoard(String player);
}
