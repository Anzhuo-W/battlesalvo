package cs3500.pa04;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Record for a join request response
 *
 * @param name name of ai player
 * @param type type of game, single player or multiplayer
 */
public record JoinJson(
    @JsonProperty("name") String name,
    @JsonProperty("game-type") GameType type) {
}
