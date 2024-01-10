package cs3500.pa04;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.controller.Controller;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GeneralPlayer;
import cs3500.pa03.model.Ship;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Proxy controller for server/AI interactions
 */
public class ProxyController implements Controller {

  private final Socket server;
  private final InputStream in;
  private final PrintStream out;
  private final GeneralPlayer player;
  private final ObjectMapper mapper = new ObjectMapper();

  private static final JsonNode VOID_RESPONSE =
      new ObjectMapper().getNodeFactory().textNode("void");

  /**
   * Construct an instance of a ProxyPlayer.
   *
   * @param server the socket connection to the server
   * @param player the instance of the player
   */
  public ProxyController(Socket server, GeneralPlayer player) {
    this.server = server;
    try {
      this.in = server.getInputStream();
      this.out = new PrintStream(server.getOutputStream());
    } catch (IOException e) {
      throw new IllegalStateException("Cannot get input/output from socket");
    }
    this.player = player;
  }


  /**
   * Listens for messages from the server as JSON in the format of a MessageJSON. When a complete
   * message is sent by the server, the message is parsed and then delegated to the corresponding
   * helper method for each message. This method stops when the connection to the server is closed
   * or an IOException is thrown from parsing malformed JSON.
   */
  public void run() {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);

      while (!this.server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);

        System.out.println("message " + message);
        delegateMessage(message);
      }
    } catch (IOException e) {
      // Disconnected from server or parsing exception
      System.err.println("Cannot read from server; invalid socket");
    }
  }

  /**
   * Determines the type of request the server has sent ("guess" or "win") and delegates to the
   * corresponding helper method with the message arguments.
   *
   * @param message the MessageJSON used to determine what the server has sent
   */
  private void delegateMessage(MessageJson message) {
    JsonNode jsonResponse = JsonUtils.serializeRecord(message);
    System.out.println(message);
    String name = message.messageName();
    JsonNode arguments = message.arguments();

    if ("join".equals(name)) {
      handleJoin(arguments);
    } else if ("setup".equals(name)) {
      handleSetup(arguments);
    } else if ("take-shots".equals(name)) {
      handleTakeShots(arguments);
    } else if ("report-damage".equals(name)) {
      handleReportDamage(arguments);
    } else if ("successful-hits".equals(name)) {
      handleSuccessfulHits(arguments);
    } else if ("end-game".equals(name)) {
      handleEndGame(arguments);
    } else {
      throw new IllegalStateException("Invalid message name");
    }
  }

  private void handleEndGame(JsonNode arguments) {
    EndGameJson end = mapper.convertValue(arguments, EndGameJson.class);

    player.endGame(end.result(), end.reason());

    JsonNode emptyJson = mapper.createObjectNode();

    MessageJson response = new MessageJson("end-game", emptyJson);

    // in for diagnosis reasons
    System.out.println(end.result());
    System.out.println(end.reason());

    // can probably also use VOID-RESPONSE instead of the previous lines...
    this.out.println(response);
    try {
      this.server.close();
    } catch (IOException e) {
      System.err.println("Socket is invalid please try again");
    }
  }

  private void handleSuccessfulHits(JsonNode arguments) {
    CoordinatesJson hits = mapper.convertValue(arguments, CoordinatesJson.class);

    player.successfulHits(hits.shots());

    JsonNode emptyJson = mapper.createObjectNode();

    MessageJson response = new MessageJson("successful-hits", emptyJson);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    this.out.println(jsonResponse);
  }

  private void handleReportDamage(JsonNode arguments) {
    List<Coord> coords = mapper.convertValue(arguments, CoordinatesJson.class).shots();

    List<Coord> hits = player.reportDamage(coords);

    CoordinatesJson hitsCoords = new CoordinatesJson(hits);

    JsonNode hitsJson = JsonUtils.serializeRecord(hitsCoords);

    MessageJson response = new MessageJson("report-damage", hitsJson);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);

    this.out.println(jsonResponse);
  }

  private void handleTakeShots(JsonNode arguments) {
    List<Coord> shots = player.takeShots();

    CoordinatesJson coords = new CoordinatesJson(shots);

    JsonNode coordsJson = JsonUtils.serializeRecord(coords);

    MessageJson response = new MessageJson("take-shots", coordsJson);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    this.out.println(jsonResponse);
  }

  // SETUP JSON
  private void handleSetup(JsonNode arguments) {
    SetupJson setupArgs = mapper.convertValue(arguments, SetupJson.class);

    List<Ship> shipList = player.setup(setupArgs.height(), setupArgs.width(), setupArgs.specs());

    List<ShipAdapter> shipAdapterList = new ArrayList<>();

    for (Ship s : shipList) {
      shipAdapterList.add(new ShipAdapter(s));
    }

    FleetJson fleet = new FleetJson(shipAdapterList);

    JsonNode fleetJson = JsonUtils.serializeRecord(fleet);

    MessageJson response = new MessageJson("setup", fleetJson);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    System.out.println(jsonResponse);

    this.out.println(jsonResponse);
  }

  // JOIN JSON
  private void handleJoin(JsonNode arguments) {
    JoinJson joinJson = new JoinJson(this.player.name(), GameType.SINGLE);

    JsonNode join = JsonUtils.serializeRecord(joinJson);

    MessageJson response = new MessageJson("join", join);

    JsonNode jsonResponse = JsonUtils.serializeRecord(response);

    this.out.println(jsonResponse);
  }
}
