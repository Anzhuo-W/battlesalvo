package cs3500.pa03.model;

import java.util.List;

/**
 * Interface for the model: read only access to the shot coordinates inside this container,
 * but writable access to number of shots that the controller should expect from user/ai
 */
public interface ShotCoordState {

  List<Integer> getShots();

  void setNumExpecting(int n);
}

