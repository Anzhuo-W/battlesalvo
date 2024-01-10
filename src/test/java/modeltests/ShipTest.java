package modeltests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.Coord;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipType;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for Ship
 */
public class ShipTest {
  ArrayList<Coord> coordList;

  /**
   * Init method to create list of coordinates for testing
   */
  @BeforeEach
  public void init() {
    coordList = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      coordList.add(new Coord(i, 0, "s"));
    }
  }

  /**
   * Tests a normal hit that does not sink a ship
   */
  @Test
  public void testHit() {
    Ship ship = new Ship(ShipType.BATTLESHIP, coordList);
    assertTrue(ship.hitThisShip(new Coord(0, 0)));
    assertEquals("H", coordList.get(0).getStatus());
    assertEquals("s", coordList.get(1).getStatus());
  }

  /**
   * Tests a shot that misses
   */
  @Test
  public void testMiss() {
    Ship ship = new Ship(ShipType.BATTLESHIP, coordList);
    assertFalse(ship.hitThisShip(new Coord(4, 0)));
    for (Coord c : coordList) {
      assertEquals("s", c.getStatus());
    }
  }

  /**
   * Tests a shot that sinks a ship
   */
  @Test
  public void testHitSunk() {
    for (int i = 0; i < 3; i++) {
      coordList.get(i).updateStatus("H");
    }
    Ship ship = new Ship(ShipType.BATTLESHIP, coordList);
    assertTrue(ship.hitThisShip(new Coord(3, 0)));
    assertTrue(ship.isSunk());
    for (Coord c : coordList) {
      assertEquals("X", c.getStatus());
    }
  }

  /**
   * Tests correct detection of when a ship has sunk
   */
  @Test
  public void testSunk() {
    Ship ship = new Ship(ShipType.BATTLESHIP, coordList);

    assertFalse(ship.isSunk());
    ship.updateAll("H");
    assertTrue(ship.isSunk());
  }

  /**
   * Tests updating all coordinates to a certain status
   */
  @Test
  public void testUpdateAll() {
    Ship ship = new Ship(ShipType.BATTLESHIP, coordList);

    for (Coord c : coordList) {
      assertEquals("s", c.getStatus());
    }

    ship.updateAll("X");

    for (Coord c : coordList) {
      assertEquals("X", c.getStatus());
    }
  }
}
