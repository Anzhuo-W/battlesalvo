package modeltests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.Board;
import cs3500.pa03.model.BoardImpl;
import cs3500.pa03.model.Coord;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests BoardImpl class
 */
public class BoardTest {
  Board board;

  /**
   * Initalizes a default board where all squares are set to "_" water status
   */
  @BeforeEach
  public void init() {
    board = new BoardImpl();
    board.setBoard(4, 4);
  }

  /**
   * Tests updating one coordinate
   */
  @Test
  public void testUpdate() {
    Coord c = new Coord(2, 3, "S");
    assertEquals("_", board.getStatus(3, 2));
    board.updateOne(c);
    assertEquals("S", board.getStatus(3, 2));
    assertEquals("_", board.getStatus(2, 2));
  }

  /**
   * Tests updating a list of coordinates
   */
  @Test
  public void testUpdateAll() {
    ArrayList<Coord> listOfCoords = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      listOfCoords.add(new Coord(i, 2, "H"));
    }

    for (int i = 0; i < 4; i++) {
      assertEquals("_", board.getStatus(2, i));
    }

    board.updateMany(listOfCoords);

    for (int i = 0; i < 4; i++) {
      assertEquals("H", board.getStatus(2, i));
    }
  }

  /**
   * Tests getting status of board coordinates
   */
  @Test
  public void testGetStatus() {
    board.updateOne(new Coord(3, 3, "M"));
    assertEquals("_", board.getStatus(0, 1));
    assertEquals("M", board.getStatus(3, 3));
  }

  /**
   * Tests allEmpty() method
   */
  @Test
  public void testAllEmpty() {
    ArrayList<Coord> listOfCoords = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      listOfCoords.add(new Coord(2, i, "M"));
    }
    assertTrue(board.allEmpty(listOfCoords));
    board.updateMany(listOfCoords);
    assertEquals("M", board.getStatus(3, 2));
    assertTrue(board.allEmpty(listOfCoords));
    board.updateOne(new Coord(2, 3, "H"));
    assertFalse(board.allEmpty(listOfCoords));
  }

  /**
   * Tests numEmpty() method should return expected number of empty nonship/nonhit squares
   */
  @Test
  public void testNumEmpty() {
    assertEquals(16, board.numEmpty());
    ArrayList<Coord> listOfCoords = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      listOfCoords.add(new Coord(2, i, "H"));
    }
    board.updateMany(listOfCoords);
    assertEquals(12, board.numEmpty());
  }

  /**
   * Tests whether a deep copy is actually created
   */
  @Test
  public void testGetBoard() {
    board.updateMany(Arrays.asList(new Coord(1, 3, "M"),
        new Coord(2, 2, "H")));
    // confirms board contents are as expected
    assertEquals("M", board.getStatus(3, 1));
    assertEquals("H", board.getStatus(2, 2));
    assertEquals("_", board.getStatus(1, 2));

    ArrayList<ArrayList<String>> copy = board.getBoard();

    assertNotSame(copy.get(3).get(1), board.getStatus(3, 1));
    assertEquals(copy.get(3).get(1), board.getStatus(3, 1));
    assertEquals("M", copy.get(3).get(1));

    assertNotSame(copy.get(2).get(2), board.getStatus(2, 2));
    assertEquals(copy.get(2).get(2), board.getStatus(2, 2));
    assertEquals("H", copy.get(2).get(2));
  }
}
