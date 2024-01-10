package cs3500.pa03.controller;

import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GeneralPlayer;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.model.ShotNumState;
import cs3500.pa03.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class
 */
public class ManualGameController implements Controller {
  private final GeneralPlayer user;
  private final GeneralPlayer ai;
  private final View viewer;
  private int height;
  private int width;
  private final ShotNumState currentRoundShots;
  private final ShotNumState aiShotsRemaining;

  /**
   * Constructor
   *
   * @param player1 first player
   * @param player2 second player
   * @param display view to display game to
   * @param currShots user's current volley
   * @param aiShots ai's current volley
   */
  public ManualGameController(GeneralPlayer player1,
                              GeneralPlayer player2,
                              View display,
                              ShotNumState currShots,
                              ShotNumState aiShots) {
    user = player1;
    ai = player2;
    viewer = display;
    currentRoundShots = currShots;
    aiShotsRemaining = aiShots;
  }

  /**
   * Gets user input for board size and updates state accordingly
   */
  private void boardSize() {
    while (true) {
      List<String> response = viewer.displayPrompt("Please enter a valid height "
          + "and width below."
          + "\nYou should only enter 2 valid integers.\nIf you enter more "
          + "than the expected number of inputs, only the first 2 will be taken."
          + "\n------------------------------------------------------\n", 2);
      try {
        height = Integer.parseInt(response.get(0));
        width = Integer.parseInt(response.get(1));
      } catch (NumberFormatException e) {
        viewer.displayContent(
            "You've entered invalid dimensions :/ . Please remember that the height "
                + "and width\n"
                + "of the game must be in the range [6, 15], inclusive. Try again.\n");
        continue;
      }
      if (height < 6 || height > 15 || width < 6 || width > 15 || response.size() != 2) {
        viewer.displayContent(
            "You've entered invalid dimensions :/ . Please remember that the height and "
                + "width\n"
                + "of the game must be in the range [6, 15], inclusive. Try again.\n");
        continue;
      }
      break;
    }
  }

  /**
   * Gets ship specifications
   */
  public void getShipNum() {
    String originalPrompt = "Please enter your fleet in the order "
        + "[Carrier, Battleship, Destroyer, Submarine]. Remember, your fleet "
        + "may not exceed size " + Math.min(width, height) + ".\n Only the first 4 inputs you enter"
        + " will be taken.";
    List<Integer> fleetSize;
    first: while (true) {
      List<String> userResponse = viewer.displayPrompt(originalPrompt, 4);
      fleetSize = new ArrayList<>();
      for (String s : userResponse) {
        try {
          fleetSize.add(Integer.parseInt(s));
        } catch (NumberFormatException e) {
          viewer.displayContent("-------------------------------------------------"
              + "-------------------------------\nUh Oh! You've entered invalid fleet sizes.\n");
          continue first;
        }
      }
      int sum = 0;
      for (Integer i : fleetSize) {
        if (i < 1) {
          viewer.displayContent("-------------------------------------------------"
              + "-------------------------------\nUh Oh! You've entered invalid fleet sizes.\n");
          continue first;
        } else {
          sum += i;
        }
      }
      if (sum <= Math.min(width, height)) {
        break;
      } else {
        viewer.displayContent("-------------------------------------------------"
            + "-------------------------------\nUh Oh! You've entered invalid fleet sizes.\n");
      }
    }
    Map<ShipType, Integer> shipMap = new HashMap<>();
    for (ShipType s : ShipType.values()) {
      shipMap.put(s, fleetSize.get(s.ordinal()));
    }
    user.setup(height, width, shipMap);
    ai.setup(height, width, shipMap);
  }

  /**
   * Asks user for one round of shots, and updates the user's ShotContainer with the shots they
   * inputted
   */
  private void takeShots() {
    viewer.displayContent("Opponent board:\n");
    viewer.displayBoard("opponent");
    viewer.displayContent("\nYour board:\n");
    viewer.displayBoard("user");
    int numberToShoot = currentRoundShots.getNumToExpect();
    first: while (true) {
      List<String> userResponse = viewer.displayPrompt("Please Enter " + numberToShoot
          + " Shots:\n"
          + "------------------------------------------------------------------\n",
          numberToShoot * 2);
      List<Integer> convertedResponse = new ArrayList<>();
      int counter = 0;
      while (counter < userResponse.size()) {
        String row = userResponse.get(counter + 1);
        String col = userResponse.get(counter);
        try {
          int rowInt = Integer.parseInt(row);
          int colInt = Integer.parseInt(col);
          if (rowInt < 0 || rowInt > height - 1 || colInt < 0 || colInt > width - 1) {
            viewer.displayContent("\nEntered an invalid input! Try again :|\n");
            continue first;
          }
          convertedResponse.add(rowInt);
          convertedResponse.add(colInt);
        } catch (NumberFormatException e) {
          viewer.displayContent("\nEntered an invalid input! Try again :|\n");
          continue first;
        }
        counter += 2;
      }
      currentRoundShots.setShots(convertedResponse);
      break;
    }
  }

  /**
   * Loops until either one or both players have no expected shots left ie they don't have any more
   * ships
   */
  private void gameLoop() {
    while (true) {
      takeShots();

      List<Coord> userShots = user.takeShots();
      List<Coord> aiShots = ai.takeShots();
      List<Coord> aiSuccesses = user.reportDamage(aiShots);
      List<Coord> userSuccesses = ai.reportDamage(userShots);
      StringBuilder userSuccess = new StringBuilder();
      StringBuilder aiSuccess = new StringBuilder();

      for (Coord c : userSuccesses) {
        userSuccess.append("[" + c.getColumn() + ", " + c.getRow() + "]");
        userSuccess.append(" ");
      }
      for (Coord c : aiSuccesses) {
        aiSuccess.append("[" + c.getColumn() + ", " + c.getRow() + "]");
        aiSuccess.append(" ");
      }

      user.successfulHits(userSuccesses);
      ai.successfulHits(aiSuccesses);

      viewer.displayContent(
          ("You took these shots that were successful: " + userSuccess).trim() + "\n");
      viewer.displayContent(("The computer took these shots that were successful: "
          + aiSuccess).trim() + "\n");

      if (currentRoundShots.getNumToExpect() == 0 && aiShotsRemaining.getNumToExpect() != 0) {
        viewer.displayContent("You lost :(");
        break;
      } else if (currentRoundShots.getNumToExpect() == 0
          && aiShotsRemaining.getNumToExpect() == 0) {
        viewer.displayContent("You tied :|");
        break;
      } else if (currentRoundShots.getNumToExpect() != 0
          && aiShotsRemaining.getNumToExpect() == 0) {
        viewer.displayContent("You won :)");
        break;
      }
    }
  }

  /**
   * Runs program
   */
  public void run() {
    try {
      viewer.displayContent("Hello! Welcome to the OOD BattleSalvo Game!");
    } catch (RuntimeException e) {
      System.err.println("Cannot display output");
      return;
    }
    try {
      boardSize();
      getShipNum();
      gameLoop();
    } catch (IllegalStateException e) {
      System.err.println(e);
    }
  }
}
