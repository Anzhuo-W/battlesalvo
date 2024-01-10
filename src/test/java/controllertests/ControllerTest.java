package controllertests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import cs3500.pa03.controller.ManualGameController;
import cs3500.pa03.model.Board;
import cs3500.pa03.model.BoardImpl;
import cs3500.pa03.model.ComputerPlayer;
import cs3500.pa03.model.GeneralPlayer;
import cs3500.pa03.model.ShotContainer;
import cs3500.pa03.model.UserPlayer;
import cs3500.pa03.view.ConsoleView;
import cs3500.pa03.view.View;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for ManualGameController class
 */
public class ControllerTest {
  Readable input = null;
  Appendable output;
  ConsoleView viewer;
  ShotContainer currentRoundShots = new ShotContainer();
  ShotContainer aiShots = new ShotContainer();
  GeneralPlayer user;
  GeneralPlayer ai;
  ManualGameController controller;
  Random randomGenerator = new Random(2);
  Board myBoard = new BoardImpl();
  Board aiBoard = new BoardImpl();
  Board opponentAiBoard = new BoardImpl();
  Board userBoard = new BoardImpl();

  /**
   * Instantiates the controller and other variables needed for tests
   */
  @BeforeEach
  public void init() {
    try {
      input = new InputStreamReader(new FileInputStream("src/test/controllerInputs.txt"));
      output = new StringBuilder();
    } catch (IOException e) {
      fail();
    }
    user = new UserPlayer(randomGenerator, currentRoundShots, myBoard, aiBoard);
    ai = new ComputerPlayer(randomGenerator, aiShots, opponentAiBoard, userBoard);
    View consoleView = new ConsoleView(myBoard, aiBoard, input, output);

    controller = new ManualGameController(user, ai, consoleView, currentRoundShots, aiShots);
  }

  @Test
  public void testRun() {
    String expected = "";
    try {
      expected = Files.readString(Path.of("src/test/controllerOutput.txt"));
    } catch (IOException e) {
      fail();
    }
    controller.run();
    assertEquals(expected, output.toString());
  }
}
