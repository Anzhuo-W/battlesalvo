package cs3500.pa04;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.Coord;
import java.util.List;

/**
 * Record to serialize a set of coords
 *
 * @param shots list of coords to represent
 */
public record CoordinatesJson(
    @JsonProperty("coordinates") List<Coord> shots) {
}
