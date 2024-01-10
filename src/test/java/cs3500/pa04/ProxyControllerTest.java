package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.model.Board;
import cs3500.pa03.model.BoardImpl;
import cs3500.pa03.model.ComputerPlayer;
import cs3500.pa03.model.GeneralPlayer;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.model.ShotContainer;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test correct responses for different requests from the socket using a Mock Socket (mocket)
 */
public class ProxyControllerTest {

  private ByteArrayOutputStream testLog;
  private ProxyController controller;

  private GeneralPlayer player;
  private final Random randomGenerator = new Random(20);

  private Board aiBoard;
  private Board opponentBoard;
  private ShotContainer shotContainer;

  private JsonNode joinNode;
  private JsonNode setupNode;
  private JsonNode takeShotsNode;

  /**
   * Reset the test log before each test is run.
   */
  @BeforeEach
  public void setup() {
    this.testLog = new ByteArrayOutputStream(2048);
    aiBoard = new BoardImpl();
    opponentBoard = new BoardImpl();
    shotContainer = new ShotContainer();
    player = new ComputerPlayer(randomGenerator, shotContainer, aiBoard, opponentBoard);
    assertEquals("", logToString());

    Map<ShipType, Integer> specs = new HashMap<>();
    for (ShipType s : ShipType.values()) {
      specs.put(s, 1);
    }

    // Prepare sample messages
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.createObjectNode();

    MessageJson join = new MessageJson("join", node);
    this.joinNode = createSampleMessage("join", join);

    SetupJson setup = new SetupJson(6, 7, specs);
    this.setupNode = createSampleMessage("setup", setup);

    MessageJson takeNode = new MessageJson("take-shots", node);
    this.takeShotsNode = JsonUtils.serializeRecord(takeNode);


  }

  /**
   * Check that the server returns a guess when given a hint.
   */
  @Test
  public void testJoin() {
    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(joinNode.toString()));

    // Create a Dealer
    this.controller = new ProxyController(socket, player);

    // run the dealer and verify the response
    this.controller.run();

    String expected = "{\"method-name\":\"join\",\"arguments\":{\"name\":\"stephanie-xu\","
        + "\"game-type\":\"SINGLE\"}}\n";
    assertEquals(expected, logToString());
  }

  /**
   * Tests setup in proxy controller
   */
  @Test
  public void testSetup() {
    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(setupNode.toString()));

    // Create a Dealer
    this.controller = new ProxyController(socket, player);

    // run the dealer and verify the response
    this.controller.run();
    System.out.println(testLog);
    assertTrue(logToString().contains("{\"method-name\":\"setup\","
        + "\"arguments\":{\"fleet\":[{\"coord\":{\"x\":"));
  }

  /**
   * Tests taking shots
   */
  @Test
  public void testTakeShots() {
    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(setupNode.toString(),
        takeShotsNode.toString()));

    // Create a Dealer
    this.controller = new ProxyController(socket, player);

    // run the dealer and verify the response
    this.controller.run();

    assertTrue(logToString().contains("method-name"));
    assertTrue(logToString().contains("take-shots"));
    assertTrue(logToString().contains("arguments"));
    assertTrue(logToString().contains("coordinates"));
  }

  /**
   * Tests reporting damage jsons
   */
  @Test
  public void testReportDamage() {
    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(setupNode.toString(),
        takeShotsNode.toString()));

  }

  /**
   * Create a MessageJson for some name and arguments.
   *
   * @param messageName name of the type of message; "hint" or "win"
   * @param messageObject object to embed in a message json
   * @return a MessageJson for the object
   */
  private JsonNode createSampleMessage(String messageName, Record messageObject) {
    MessageJson messageJson = new MessageJson(messageName,
        JsonUtils.serializeRecord(messageObject));
    return JsonUtils.serializeRecord(messageJson);
  }


  /**
   * Converts the ByteArrayOutputStream log to a string in UTF_8 format
   *
   * @return String representing the current log buffer
   */
  private String logToString() {
    return testLog.toString(StandardCharsets.UTF_8);
  }
}