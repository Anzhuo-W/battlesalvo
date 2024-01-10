package modeltests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.Board;
import cs3500.pa03.model.BoardImpl;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GeneralPlayer;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.model.ShotContainer;
import cs3500.pa03.model.UserPlayer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the UserPlayer class
 */
public class UserPlayerTest {
  ShotContainer shotContainer;
  Random randomGenerator = new Random(2);
  GeneralPlayer player;
  Map<ShipType, Integer> shipMap = new HashMap<>();
  Board userBoard = new BoardImpl();
  Board aiBoard = new BoardImpl();

  /**
   * Run before each test to set up variables
   */
  @BeforeEach
  public void init() {
    shotContainer = new ShotContainer();
    player = new UserPlayer(randomGenerator, shotContainer, userBoard, aiBoard);
    for (ShipType s : ShipType.values()) {
      shipMap.put(s, 1);
    }
  }

  /**
   * Tests setup method in general player that user player inherits
   */
  @Test
  public void testSetup() {
    List<Ship> ships = player.setup(6, 6, shipMap);
    assertEquals(4, ships.size());
    assertEquals(4, shotContainer.getNumToExpect());
    for (Ship ship : ships) {
      assertFalse(ship.isSunk());
    }
    assertEquals(userBoard.getBoard().size(), aiBoard.getBoard().size());

    int emptySpaceCount = 0;
    int destroyerCount = 0;
    int battleShipCount = 0;
    int carrierCount = 0;
    int subCount = 0;

    for (ArrayList<String> row : userBoard.getBoard()) {
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

    assertEquals(18, emptySpaceCount);
    assertEquals(5, battleShipCount);
    assertEquals(6, carrierCount);
    assertEquals(3, subCount);
    assertEquals(4, destroyerCount);
  }

  /**
   * Tests taking shots, userplayer overridden method
   */
  @Test
  public void testTakeShots() {
    shotContainer.setShots(Arrays.asList(1, 2, 3, 4, 5, 6, 1, 3));
    player.setup(6, 6, shipMap);
    List<Coord> shots = player.takeShots();

    assertTrue(shots.get(0).samePosition(new Coord(2, 1)));
    assertTrue(shots.get(1).samePosition(new Coord(4, 3)));
    assertTrue(shots.get(2).samePosition(new Coord(6, 5)));
  }

  /**
   * Tests report damage, a generalplayer method that userplayer inherits
   */
  @Test
  public void testReportDamage() {
    player.setup(6, 6, shipMap);
    Random r2 = new Random(2);
    ShotContainer s2 = new ShotContainer();
    Board board2 = new BoardImpl();
    Board aiBoard2 = new BoardImpl();
    GeneralPlayer userPlayer2 = new UserPlayer(r2, s2, board2, aiBoard2);
    userPlayer2.setup(6, 6, shipMap);
    List<Coord> mockShots = new ArrayList<>();
    for (int i = 0; i < 5; i += 2) {
      mockShots.add(new Coord(i, i + 1));
    }
    player.reportDamage(mockShots);
    userPlayer2.reportDamage(mockShots);
    assertEquals(player.reportDamage(mockShots), userPlayer2.reportDamage(mockShots));
    assertEquals(s2.getNumToExpect(), shotContainer.getNumToExpect());
  }

  /**
   * Tests successfulhits which userplayer inherits from generalplayer
   */
  @Test
  public void testSuccessfulHits() {
    player.setup(6, 6, shipMap);
    List<Integer> mockShotsTaken = new ArrayList<>();
    List<Coord> mockShots = new ArrayList<>();
    for (int i = 0; i < 5; i += 2) {
      mockShotsTaken.add(i);
      mockShotsTaken.add(i + 1);
      mockShots.add(new Coord(i, i + 1));
    }
    mockShotsTaken.add(3);
    mockShotsTaken.add(1);
    shotContainer.setShots(mockShotsTaken);
    player.takeShots();
    player.successfulHits(mockShots);
  }
}
