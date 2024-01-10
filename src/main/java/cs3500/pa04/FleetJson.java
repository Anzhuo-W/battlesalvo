package cs3500.pa04;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Record representing a fleet of ships
 *
 * @param ships list of ship adapters
 */
public record FleetJson(
    @JsonProperty("fleet") List<ShipAdapter> ships) {
}
