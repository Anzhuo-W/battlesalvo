package cs3500.pa03.model;

import java.util.ArrayList;

/**
 * Read only interface for board
 */
public interface BoardState {
  ArrayList<ArrayList<String>> getBoard();
}
