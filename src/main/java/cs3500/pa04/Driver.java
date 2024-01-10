package cs3500.pa04;

import cs3500.pa03.controller.Controller;
import cs3500.pa03.controller.ManualGameController;
import cs3500.pa03.model.Board;
import cs3500.pa03.model.BoardImpl;
import cs3500.pa03.model.ComputerPlayer;
import cs3500.pa03.model.GeneralPlayer;
import cs3500.pa03.model.ShotContainer;
import cs3500.pa03.model.UserPlayer;
import cs3500.pa03.view.ConsoleView;
import cs3500.pa03.view.View;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Random;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * Project entry point
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) {
    ShotContainer aiShots = new ShotContainer();
    Board aiBoard = new BoardImpl();
    Board opponentUserBoard = new BoardImpl();

    if (args.length == 0) {
      ShotContainer currentRoundShots = new ShotContainer();
      Board userBoard = new BoardImpl();
      Board opponentAiBoard = new BoardImpl();
      GeneralPlayer user = new UserPlayer(new Random(), currentRoundShots,
          userBoard, opponentAiBoard);
      GeneralPlayer ai = new ComputerPlayer(new Random(), aiShots, aiBoard, opponentUserBoard);
      View consoleView = new ConsoleView(userBoard, opponentAiBoard,
          new InputStreamReader(System.in), new PrintStream(System.out));
      Controller
          controller = new ManualGameController(user, ai, consoleView, currentRoundShots, aiShots);

      controller.run();
    } else if (args.length == 2) {
      String host = args[0];
      int port = Integer.parseInt(args[1]);
      try {
        Socket s = new Socket(host, port);
        ProxyController pc = new ProxyController(s, new ComputerPlayer(
            new Random(), aiShots, aiBoard, opponentUserBoard));
        pc.run();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      System.err.println("Invalid number of arguments given");
    }
  }
}