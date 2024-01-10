package cs3500.pa03.model;

import java.util.List;

/**
 * Shot Container interface to help manual game controller, readable access to number of shots to
 * expect, writable access to shot coordinate list
 */
public interface ShotNumState {

  int getNumToExpect();

  void setShots(List<Integer> s);
}
