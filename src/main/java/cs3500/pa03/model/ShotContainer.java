package cs3500.pa03.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Dependency injection class for communication between controller and model shot taking
 *
 */
public class ShotContainer implements ShotNumState, ShotCoordState {
  private List<Integer> currShots;
  private int numToExpect;

  /**
   * Constructor initiates fields
   */
  public ShotContainer() {
    currShots = new ArrayList<>();
    numToExpect = 0;
  }

  /**
   * Gets list of shots in this object
   *
   * @return List of integer values representing either x or y coordinate of a shot, each pair of
   *        ints is a valid shot
   */
  public List<Integer> getShots() {
    return currShots;
  }

  /**
   * Gets number of shots to expect next from the player, ie how many player ships are remaining
   *
   * @return integer number of shots
   */
  public int getNumToExpect() {
    return numToExpect;
  }

  /**
   * Sets the list of shots to the given list
   *
   * @param s new list of shots to take
   */
  public void setShots(List<Integer> s) {
    currShots = s;
  }

  /**
   * Sets number of expected shots to given number
   *
   * @param n nonnegative number of shots to expect next
   */
  public void setNumExpecting(int n) {
    numToExpect = n;
  }
}
