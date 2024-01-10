package cs3500.pa04;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.Ship;

/**
 * Ship adapter class for standardizing ship class fields/properties
 */
public class ShipAdapter {
  private Coord coord;
  private int length;
  private Direction direction;

  /**
   * Creates a ShipAdapter from a Ship
   *
   * @param ship a ship to convert to be able to send in json
   */
  public ShipAdapter(Ship ship) {
    this.coord = ship.getCoordinates().get(ship.getCoordinates().size() - 1);
    this.length = ship.getCoordinates().size();
    Coord end = ship.getCoordinates().get(0);
    if (this.coord.getColumn() == end.getColumn()) {
      this.direction = Direction.VERTICAL;
    } else {
      this.direction = Direction.HORIZONTAL;
    }
  }

  /**
   * Allows for a ShipAdapter to be recognized as json
   */
  @JsonCreator
  public ShipAdapter(
      @JsonProperty("coord") Coord coord,
      @JsonProperty("length") int length,
      @JsonProperty("direction") Direction direction) {
  }


  public Coord getCoord() {
    return coord;
  }

  public int getLength() {
    return length;
  }

  public Direction getDirection() {
    return direction;
  }
}
