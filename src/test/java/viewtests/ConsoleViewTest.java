package viewtests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import cs3500.pa03.model.Board;
import cs3500.pa03.model.BoardImpl;
import cs3500.pa03.model.Coord;
import cs3500.pa03.view.ConsoleView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the console viewer
 */
public class ConsoleViewTest {
  Readable input = null;
  Appendable output;
  ConsoleView viewer;
  Board myBoard;
  Board opponentBoard;

  /**
   * Initalizes the view, and readable and appendable objects,reads from the file testconsoleinputs
   */
  @BeforeEach
  public void init() {
    try {
      input = new InputStreamReader(new FileInputStream("src/testConsoleInputs.txt"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      fail();
    }
    myBoard = new BoardImpl();
    opponentBoard = new BoardImpl();
    output = new StringBuilder();
    viewer = new ConsoleView(myBoard, opponentBoard, input, output);
  }

  /**
   * Tests displaying a string
   */
  @Test
  public void testDisplayContent() {
    try {
      viewer.displayContent("Please display this string");
    } catch (RuntimeException e) {
      fail();
    }
    assertEquals("Please display this string", output.toString());
  }

  /**
   * Test getting user input
   */

  @Test
  public void testDisplayPrompt() {
    List<String> tempOutput = null;
    try {
      tempOutput = viewer.displayPrompt("Please display this string", 3);
    } catch (RuntimeException e) {
      fail();
    }
    assertEquals("Please", tempOutput.get(0));
    assertEquals("print", tempOutput.get(1));
    assertEquals("this", tempOutput.get(2));
  }

  /**
   * Tests displaying a board
   */
  @Test
  public void testBoard() {
    myBoard.setBoard(5, 5);
    try {
      viewer.displayBoard("user");
    } catch (RuntimeException e) {
      e.printStackTrace();
      fail();
    }
    assertEquals("_ _ _ _ _\n_ _ _ _ _\n_ _ _ _ _\n_ _ _ _ _\n_ _ _ _ _\n",
        output.toString());
  }

  /**
   * Test board that has different strings as cells
   */
  @Test
  public void testBoardTwo() {
    myBoard.setBoard(3, 5);
    myBoard.updateOne(new Coord(0, 3, "H"));
    myBoard.updateOne(new Coord(1, 0, "X"));
    myBoard.updateOne(new Coord(2, 2, "S"));
    try {
      viewer.displayBoard("user");
    } catch (RuntimeException e) {
      e.printStackTrace();
      fail();
    }
    assertEquals("_ X _\n_ _ _\n_ _ S\nH _ _\n_ _ _\n",
        output.toString());
  }

  @Test
  public void testOpponentBoardError() {
    opponentBoard.setBoard(3, 3);
    opponentBoard.updateOne(new Coord(1, 2, "H"));

    try {
      viewer.displayBoard("opponent");
    } catch (RuntimeException e) {
      e.printStackTrace();
      fail();
    }

    assertEquals("_ _ _\n_ _ _\n_ H _\n",
        output.toString());
    assertThrows(IllegalArgumentException.class, () -> viewer.displayBoard("blah"));
  }
}
