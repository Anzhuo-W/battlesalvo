package cs3500.pa03.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Abstract player class shared between manual and ai players
 */
public abstract class GeneralPlayer implements Player {
  protected int width;
  protected int height;
  protected Board myBoard;
  protected Board opponentBoard;
  protected List<Ship> ships;
  protected List<Coord> alreadyShot;
  protected List<Coord> currentRoundShots;
  protected Random generator;
  protected ShotCoordState shotContainer;
  protected List<Coord> successfulShots;

  /**
   * Constructor
   *
   * @param r random object for testing
   * @param s shot coordinate state as bridge between controller and model
   */
  public GeneralPlayer(Random r, ShotCoordState s) {
    generator = r;
    shotContainer = s;
  }

  @Override
  public abstract String name();

  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    myBoard.setBoard(width, height);
    opponentBoard.setBoard(width, height);
    this.width = width;
    this.height = height;
    alreadyShot = new ArrayList<>();
    ships = new ArrayList<>();
    successfulShots = new ArrayList<>();

    for (int i = 0; i < specifications.get(ShipType.CARRIER); i++) {
      ships.add(validShip(ShipType.CARRIER));
    }
    for (int i = 0; i < specifications.get(ShipType.BATTLESHIP); i++) {
      ships.add(validShip(ShipType.BATTLESHIP));
    }
    for (int i = 0; i < specifications.get(ShipType.DESTROYER); i++) {
      ships.add(validShip(ShipType.DESTROYER));
    }
    for (int i = 0; i < specifications.get(ShipType.SUBMARINE); i++) {
      ships.add(validShip(ShipType.SUBMARINE));
    }
    shotContainer.setNumExpecting(ships.size());
    return ships;
  }

  @Override
  public abstract List<Coord> takeShots();

  protected int numUnsunk() {
    int counter = 0;
    for (Ship ship : ships) {
      if (!ship.isSunk()) {
        counter += 1;
      }
    }
    return counter;
  }

  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    ArrayList<Coord> hits = new ArrayList<>();
    for (Coord shot : opponentShotsOnBoard) {
      if (!(myBoard.getStatus(shot.getRow(), shot.getColumn()).equals("_")
          || myBoard.getStatus(shot.getRow(), shot.getColumn()).equals("M"))) {
        hits.add(shot);
        myBoard.updateOne(new Coord(shot.getColumn(), shot.getRow(), "H"));
        for (Ship s : ships) {
          s.hitThisShip(shot);
        }
      } else {
        myBoard.updateOne(new Coord(shot.getColumn(), shot.getRow(), "M"));
      }
    }
    return hits;
  }

  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    successfulShots.addAll(shotsThatHitOpponentShips);
    for (Coord shotTaken : currentRoundShots) {
      if (shotsThatHitOpponentShips.contains(shotTaken)) {
        opponentBoard.updateOne(new Coord(shotTaken.getColumn(), shotTaken.getRow(), "H"));
      } else {
        opponentBoard.updateOne(new Coord(shotTaken.getColumn(), shotTaken.getRow(), "M"));
      }
    }
    shotContainer.setNumExpecting(Math.min(numUnsunk(), opponentBoard.numEmpty()));
  }

  @Override
  public void endGame(GameResult result, String reason) {
  }

  private List<Coord> shipPlacement(ShipType t, int boardWidth, int boardHeight, String symbol) {
    while (true) {
      Coord start = new Coord(generator.nextInt(boardWidth),
          generator.nextInt(boardHeight), symbol);
      if (!myBoard.getStatus(start.getRow(), start.getColumn()).equals("_")) {
        continue;
      }
      int endCoord;
      Coord end;
      if (generator.nextBoolean()) {
        endCoord = -t.getLength() + 1;
      } else {
        endCoord = -t.getLength() - 1;
      }
      if (generator.nextBoolean()) {
        end = new Coord(start.getColumn(), start.getRow() + endCoord, symbol);
      } else {
        end = new Coord(start.getColumn() + endCoord, start.getRow(), symbol);
      }
      if (end.getRow() >= boardHeight || end.getRow() < 0 || end.getColumn() >= boardWidth
          || end.getColumn() < 0) {
        continue;
      }
      ArrayList<Coord> shipCoords = new ArrayList<>();
      shipCoords.add(start);

      for (int i = 1; i < t.getLength(); i++) {
        if (end.getRow() < start.getRow()) {
          shipCoords.add(new Coord(start.getColumn(), start.getRow() - i, symbol));
        } else if (end.getRow() > start.getRow()) {
          shipCoords.add(new Coord(start.getColumn(), start.getRow() + i, symbol));
        } else if (end.getColumn() < start.getColumn()) {
          shipCoords.add(new Coord(start.getColumn() - i, start.getRow(), symbol));
        } else {
          shipCoords.add(new Coord(start.getColumn() + i, start.getRow(), symbol));
        }
      }
      return shipCoords;
    }
  }

  private Ship validShip(ShipType type) {
    boolean spacesTaken = false;
    List<Coord> placements = shipPlacement(type, width, height, type.toString().substring(0, 1));
    for (Coord c : placements) {
      if (!myBoard.getStatus(c.getRow(), c.getColumn()).equals("_")) {
        spacesTaken = true;
        break;
      }
    }
    while (spacesTaken) {
      placements = shipPlacement(type, width, height, type.toString().substring(0, 1));
      spacesTaken = false;
      for (Coord c : placements) {
        if (!myBoard.getStatus(c.getRow(), c.getColumn()).equals("_")) {
          spacesTaken = true;
          break;
        }
      }
    }
    myBoard.updateMany(placements);
    return new Ship(type, placements);
  }
}
