package modeltests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import cs3500.pa03.model.Board;
import cs3500.pa03.model.BoardImpl;
import cs3500.pa03.model.ComputerPlayer;
import cs3500.pa03.model.GeneralPlayer;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.model.ShotContainer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for ComputerPlayer class
 */
public class ComputerPlayerTest {
  Random randomGenerator = new Random(20);
  GeneralPlayer player;
  Board aiBoard;
  Board opponentUserBoard;
  Map<ShipType, Integer> shipMap = new HashMap<>();

  /**
   * Instantiates variables to use in tests
   */
  @BeforeEach
  public void init() {
    aiBoard = new BoardImpl();
    opponentUserBoard = new BoardImpl();
    player = new ComputerPlayer(randomGenerator, new ShotContainer(), aiBoard, opponentUserBoard);
    for (ShipType s : ShipType.values()) {
      if (s.equals(ShipType.CARRIER)) {
        shipMap.put(s, 3);
      } else {
        shipMap.put(s, 1);
      }
    }
  }

  /**
   * Tests name getter
   */
  @Test
  public void testName() {
    assertEquals("stephanie-xu", player.name());
  }

  /**
   * Tests setup method, inherited from generalplayer
   */
  @Test
  public void testSetup() {
    List<Ship> ships = player.setup(6, 6, shipMap);
    assertEquals(6, ships.size());
    for (Ship ship : ships) {
      assertFalse(ship.isSunk());
    }
    assertEquals(aiBoard.getBoard().size(), opponentUserBoard.getBoard().size());

    int emptySpaceCount = 0;
    int destroyerCount = 0;
    int battleShipCount = 0;
    int carrierCount = 0;
    int subCount = 0;

    for (ArrayList<String> row : aiBoard.getBoard()) {
      for (String col : row) {
        if (col.equals("_")) {
          emptySpaceCount++;
        } else if (col.equals("C")) {
          carrierCount++;
        } else if (col.equals("D")) {
          destroyerCount++;
        } else if (col.equals("S")) {
          subCount++;
        } else if (col.equals("B")) {
          battleShipCount++;
        }
      }
    }

    assertEquals(6, emptySpaceCount);
    assertEquals(5, battleShipCount);
    assertEquals(18, carrierCount);
    assertEquals(3, subCount);
    assertEquals(4, destroyerCount);
  }
}
