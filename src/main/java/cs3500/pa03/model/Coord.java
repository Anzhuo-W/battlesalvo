package cs3500.pa03.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class to represent a coordinate and what is on the coordinate in a game of BattleSalvo
 */
public class Coord {

  private final int row;
  private final int column;

  // one of 4 values: w (water, no hit), m (water, miss),
  // h (ship, hit), s (ship, not hit), x (ship, sunk)

  @JsonIgnore
  private String status;

  /**
   * Convenience constructor that sets default status of the coordinate to a non hit tile of water
   *
   * @param row y value of the coordinate, must be non negative
   * @param column x value of the coordinate, must be non negative
   */

  @JsonCreator
  public Coord(@JsonProperty("x") int column, @JsonProperty("y") int row) {
    this.row = row;
    this.column = column;
    status = "w";
  }

  /**
   * Constructor that takes a row and column position and a status and initiates a coord object
   *
   * @param row y value of the coordinate, must be non negative
   * @param column x value of the coordinate, must be non negative
   * @param status string status to set the coordinate as
   */
  public Coord(int row, int column, String status) {
    this(row, column);
    this.status = status;
  }

  /**
   * Checks whether this coordinate is the same position as the given coordinate
   *
   * @param other coordinate to compare locations with
   * @return boolean representing whether coordinates are the same
   */
  public boolean samePosition(Coord other) {
    return this.row == other.getRow() && this.column == other.getColumn();
  }

  /**
   * Gets x value of this coordinate
   *
   * @return int column
   */
  @JsonProperty("x")
  public int getColumn() {
    return column;
  }

  /**
   * Gets y value of this coordinate
   *
   * @return int row
   */
  @JsonProperty("y")
  public int getRow() {
    return row;
  }

  /**
   * Gets string "status" of this coordinate
   *
   * @return String status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Updates the status of this coordinate
   *
   * @param newStatus new status to be updated
   */
  public void updateStatus(String newStatus) {
    status = newStatus;
  }

  /**
   * Overrides .equals
   *
   * @param o object to compare equality
   * @return true if the other object is a coordinate and at the same position as this one
   */
  @Override
  public boolean equals(Object o) {
    return o instanceof Coord && this.samePosition((Coord) o);
  }

  /**
   * Overrides hashcode
   *
   * @return hashcode of this object, calculated based on its position
   */
  @Override
  public int hashCode() {
    return this.row * 3 + this.column * 17;
  }
}
