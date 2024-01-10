package cs3500.pa04;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.GameResult;

/**
 * Record for game end request response
 *
 * @param result result of game: win/lose/draw
 * @param reason why the result occurred
 */
public record EndGameJson(
    @JsonProperty("result") GameResult result,
    @JsonProperty("reason") String reason) {
}
