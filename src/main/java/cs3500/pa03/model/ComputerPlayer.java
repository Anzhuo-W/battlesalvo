package cs3500.pa03.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * AI player
 */
public class ComputerPlayer extends GeneralPlayer {

  /**
   * Constructor
   *
   * @param r random object, for testing purposes
   * @param s shot container
   */
  public ComputerPlayer(Random r, ShotContainer s, Board myBoard, Board aiBoard) {
    super(r, s);
    this.myBoard = myBoard;
    opponentBoard = aiBoard;
  }

  /**
   * Name of player
   *
   * @return string github username
   */
  @Override
  public String name() {
    return "stephanie-xu";
  }

  /**
   * Returns list of coordinates of shots taken: inputs the neighbors of previous successful
   * shots first, then starts randomly guessing
   *
   * @return List of coordinates representing the shots taken
   */
  @Override
  public List<Coord> takeShots() {
    List<Coord> salvo = new ArrayList<>();
    List<Coord> pointsToTry = new ArrayList<>();
    Coord possibleShot;

    // add the neighbors
    for (Coord shot : successfulShots) {
      if (shot.getColumn() - 1 >= 0) {
        possibleShot = new Coord(shot.getColumn() - 1, shot.getRow());
        if (!alreadyShot.contains(possibleShot) && !pointsToTry.contains(possibleShot)) {
          pointsToTry.add(possibleShot);
        }
      }
      if (shot.getRow() - 1 >= 0) {
        possibleShot = new Coord(shot.getColumn(), shot.getRow() - 1);
        if (!alreadyShot.contains(possibleShot) && !pointsToTry.contains(possibleShot)) {
          pointsToTry.add(possibleShot);
        }
      }
      if (shot.getColumn() + 1 < width) {
        possibleShot = new Coord(shot.getColumn() + 1, shot.getRow());
        if (!alreadyShot.contains(possibleShot) && !pointsToTry.contains(possibleShot)) {
          pointsToTry.add(possibleShot);
        }
      }
      if (shot.getRow() + 1 < height) {
        possibleShot = new Coord(shot.getColumn(), shot.getRow() + 1);
        if (!alreadyShot.contains(possibleShot) && !pointsToTry.contains(possibleShot)) {
          pointsToTry.add(possibleShot);
        }
      }
    }

    if (pointsToTry.size() > 0) {
      for (int i = 0; i < Math.min(numUnsunk(),
          Math.min(opponentBoard.numEmpty(), pointsToTry.size())); i++) {
        salvo.add(pointsToTry.get(i));
        alreadyShot.add(pointsToTry.get(i));
      }
    }

    while (salvo.size() < Math.min(numUnsunk(), opponentBoard.numEmpty())) {
      while (true) {
        int row = generator.nextInt(height);
        int col = generator.nextInt(width);

        possibleShot = new Coord(col, row);
        if (!alreadyShot.contains(possibleShot)) {
          break;
        }
      }
      salvo.add(possibleShot);
      alreadyShot.add(possibleShot);
    }
    currentRoundShots = salvo;
    return salvo;
  }
}
