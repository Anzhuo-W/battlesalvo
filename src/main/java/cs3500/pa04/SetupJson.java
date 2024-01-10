package cs3500.pa04;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.ShipType;
import java.util.Map;

/**
 * Record representing client response for setup request
 *
 * @param width width of board (max x coord)
 * @param height height of board (max y coord)
 * @param specs the # of each type of ship to put in the board, represented in a hashmap
 */
public record SetupJson(
    @JsonProperty("width") int width,
    @JsonProperty("height") int height,
    @JsonProperty("fleet-spec") Map<ShipType, Integer> specs) {
}
