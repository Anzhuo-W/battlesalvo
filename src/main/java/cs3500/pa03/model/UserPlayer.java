package cs3500.pa03.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A manually controlled player
 */
public class UserPlayer extends GeneralPlayer {

  /**
   * Constructor
   *
   * @param r Random number generator, taken in for testing
   * @param currentRoundShots Shot container for the shots the user took in the current salvo stage
   * @param myBoard user's board
   * @param aiBoard information the user knows about the computer opponent's board
   */
  public UserPlayer(Random r, ShotCoordState currentRoundShots, Board myBoard, Board aiBoard) {
    super(r, currentRoundShots);
    this.myBoard = myBoard;
    this.opponentBoard = aiBoard;
  }

  @Override
  public String name() {
    return "user. created by Stephanie";
  }

  /**
   * Takes shots from the dependency inject list of integer shots
   * thats shared by this player and the controller
   * that it's a field of
   *
   * @return list of coordinates of the shots that the user took
   */
  @Override
  public List<Coord> takeShots() {
    int i = 0;
    ArrayList<Coord> shots = new ArrayList<>();
    while (i < shotContainer.getShots().size()) {
      Coord shot = new Coord(shotContainer.getShots().get(i + 1), shotContainer.getShots().get(i));
      shots.add(shot);
      i += 2;
    }
    currentRoundShots = shots;

    return shots;
  }
}
