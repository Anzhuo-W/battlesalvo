package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.model.Coord;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the ship adapter methods
 */
public class ShipAdapterTest {
  private ShipAdapter adapter;
  private ShipAdapter vertical;

  /**
   * Initializes the ship adapters w ships
   */
  @BeforeEach
  public void init() {
    List<Coord> coordList = new ArrayList<>();
    List<Coord> coordListVert = new ArrayList<>();
    for (int i = 4; i >= 0; i -= 1) {
      coordList.add(new Coord(i, 2));
      coordListVert.add(new Coord(2, i));
    }
    Ship shipVertical = new Ship(ShipType.BATTLESHIP, coordListVert);
    Ship ship = new Ship(ShipType.BATTLESHIP, coordList);
    adapter = new ShipAdapter(ship);
    vertical = new ShipAdapter(shipVertical);
  }

  @Test
  public void testGetCoord() {
    assertEquals(new Coord(0, 2), adapter.getCoord());
    assertEquals(new Coord(2, 0), vertical.getCoord());
  }

  @Test
  public void testGetLength() {
    assertEquals(5, adapter.getLength());
    assertEquals(5, vertical.getLength());
  }

  @Test
  public void testGetDir() {
    assertEquals(Direction.HORIZONTAL, adapter.getDirection());
  }
}
