package cs3500.pa03.model;

/**
 * Enum representing the different possible ship types in BattleSalvo and their lengths
 */
public enum ShipType {
  CARRIER(6),
  BATTLESHIP(5),
  DESTROYER(4),
  SUBMARINE(3);

  private final int length;

  /**
   * Private enum constructor
   *
   * @param length length of the ship to be created
   */
  ShipType(int length) {
    this.length = length;
  }

  public int getLength() {
    return length;
  }

  /**
   * Returns the ship type from a given ship length
   *
   * @param value length
   * @return ShipType
   */
  public static ShipType getShipType(int value) {
    if (value == 6) {
      return CARRIER;
    } else if (value == 5) {
      return BATTLESHIP;
    } else if (value == 4) {
      return DESTROYER;
    } else if (value == 3) {
      return SUBMARINE;
    } else {
      throw new IllegalArgumentException("Not a valid length of ship!");
    }
  }
}
