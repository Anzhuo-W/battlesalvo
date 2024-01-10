package cs3500.pa03.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single player's battleship board layout
 */
public class BoardImpl implements Board {
  private final List<List<String>> board;

  /**
   * Constructor, initiates a blank board
   */
  public BoardImpl() {
    board = new ArrayList<>();
  }

  /**
   * Initiates a board of the given width and height, and sets all cells to water (default)
   *
   * @param width width of the board, x value range
   * @param height height of the board, y value range
   */
  public void setBoard(int width, int height) {
    for (int h = 0; h < height; h++) {
      board.add(new ArrayList<>());
      for (int w = 0; w < width; w++) {
        board.get(h).add("_");
      }
    }
  }

  /**
   * Updates the cell at the given coordinate with the given status
   *
   * @param toUpdate target cell to be updated
   */
  public void updateOne(Coord toUpdate) {
    board.get(toUpdate.getRow()).set(toUpdate.getColumn(), toUpdate.getStatus());
  }

  /**
   * Updates this board with a list of coordinates
   *
   * @param toUpdate coordinates to update statuses
   */
  public void updateMany(List<Coord> toUpdate) {
    for (Coord c : toUpdate) {
      updateOne(c);
    }
  }

  /**
   * Gets the current status of the given location
   *
   * @param row y coordinate of position
   * @param col x coordiinate of position
   * @return string of what is currently at the location
   */
  public String getStatus(int row, int col) {
    return board.get(row).get(col);
  }

  /**
   * Checks whether all coordinates in the given list are not occupied by a ship
   *
   * @param toCheck coordinates to check whether empty or not
   * @return boolean true if all coordinates are water cells
   */
  public boolean allEmpty(List<Coord> toCheck) {
    for (Coord c : toCheck) {
      if (!(getStatus(c.getRow(), c.getColumn()).equals("_")
          || getStatus(c.getRow(), c.getColumn()).equals("M"))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Counts number of empty squares on board
   *
   * @return int number of empty/water cells on board
   */
  public int numEmpty() {
    int count = 0;
    for (List<String> row : board) {
      for (String col : row) {
        if (col.equals("_")) {
          count += 1;
        }
      }
    }
    return count;
  }

  /**
   * Returns the contents of this board
   *
   * @return a deep copy of this board as a 2D arraylist of strings
   */
  public ArrayList<ArrayList<String>> getBoard() {
    ArrayList<ArrayList<String>> boardCopy = new ArrayList<>();
    for (List<String> row : board) {
      ArrayList<String> rowCopy = new ArrayList<>();
      boardCopy.add(rowCopy);
      for (String col : row) {
        StringBuilder s = new StringBuilder(col);
        rowCopy.add(s.toString());
      }
    }
    return boardCopy;
  }
}
