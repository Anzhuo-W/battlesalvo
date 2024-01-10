package modeltests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.Coord;
import java.awt.Point;
import org.junit.jupiter.api.Test;

/**
 * Tests Coord class methods
 */
public class CoordTest {

  /**
   * Tests samePosition method in coordinate
   */
  @Test
  public void testSamePos() {
    Coord origin = new Coord(0, 0);
    Coord same = new Coord(0, 0, "h");
    Coord diff = new Coord(0, 1);

    assertTrue(origin.samePosition(same));
    assertFalse(origin.samePosition(diff));
  }

  /**
   * Tests getter
   */
  @Test
  public void testGetStatus() {
    Coord origin = new Coord(0, 0);
    Coord otherCoordinate = new Coord(0, 0, "h");

    assertEquals("w", origin.getStatus());
    assertEquals("h", otherCoordinate.getStatus());
  }

  /**
   * Tests getting the rows and column values of a coordinate
   */
  @Test
  public void testGetRowCol() {
    Coord coordinate = new Coord(3, 1);

    assertEquals(1, coordinate.getRow());
    assertEquals(3, coordinate.getColumn());
  }

  /**
   * Tests updating status
   */
  @Test
  public void testUpdate() {
    Coord origin = new Coord(0, 0);
    assertEquals("w", origin.getStatus());
    origin.updateStatus("x");
    assertEquals("x", origin.getStatus());
  }

  /**
   * Tests hash code equality and overridden .equals()
   */
  @Test
  public void testEquals() {
    Coord origin = new Coord(0, 0);
    Coord originTwo = new Coord(0, 0, "s");
    Coord notOrigin = new Coord(0, 1, "w");

    assertEquals(origin, originTwo);
    assertEquals(origin.hashCode(), originTwo.hashCode());

    assertNotSame(origin, notOrigin);
    assertNotSame(origin, new Point(1, 1));
  }
}
