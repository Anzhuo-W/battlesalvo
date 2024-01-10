package cs3500.pa03.model;

import java.util.List;

/**
 * Class to represent a ship on a BattleSalvo board
 */
public class Ship {
  private final ShipType type;
  private final List<Coord> coordinates;

  /**
   * Constructor for Ship
   *
   * @param type        ShipType of this ship: submarine, battleship, destroyer, or carrier
   * @param coordinates list of coordinates that this ship will occupy
   */
  public Ship(ShipType type, List<Coord> coordinates) {
    this.type = type;
    this.coordinates = coordinates;
  }

  /**
   * Checks whether the given coordinate hits this ship
   *
   * @param shot coordinate to check
   * @return boolean for whether this ship is hit
   */
  public boolean hitThisShip(Coord shot) {
    for (Coord c : coordinates) {
      if (c.samePosition(shot)) {
        c.updateStatus("H");
        if (this.isSunk()) {
          updateAll("X");
        }
        return true;
      }
    }
    return false;
  }

  /**
   * Updates all coordinates on this ship with the given status
   *
   * @param status string of new status
   */
  public void updateAll(String status) {
    for (Coord c : coordinates) {
      c.updateStatus(status);
    }
  }

  /**
   * Checks whether all coordinates this ship is on have been hit
   *
   * @return boolean representing whether this ship has sunk
   */
  public boolean isSunk() {
    for (Coord c : coordinates) {
      if (!c.getStatus().equals("H") && !c.getStatus().equals("X")) {
        return false;
      }
    }
    return true;
  }

  public List<Coord> getCoordinates() {
    return this.coordinates;
  }
}