package cs3500.pa03.model;

import java.util.List;

/**
 * Modifiable interface for board, extending the read only boardstate
 */
public interface Board extends BoardState {

  void setBoard(int width, int height);

  void updateOne(Coord toUpdate);

  void updateMany(List<Coord> toUpdate);

  String getStatus(int row, int col);

  boolean allEmpty(List<Coord> toCheck);

  int numEmpty();
}
