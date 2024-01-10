package modeltests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.model.ShotContainer;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the shot container class
 */
public class ShotContainerTest {
  ShotContainer shots;

  @BeforeEach
  public void init() {
    shots = new ShotContainer();
  }

  @Test
  public void testShots() {
    assertEquals(0, shots.getShots().size());
    List<Integer> ints = Arrays.asList(1, 2, 3, 4);
    shots.setShots(ints);
    assertEquals(4, shots.getShots().size());
  }

  @Test
  public void testNumToExpect() {
    assertEquals(0, shots.getNumToExpect());
    shots.setNumExpecting(5);
    assertEquals(5, shots.getNumToExpect());
  }
}
