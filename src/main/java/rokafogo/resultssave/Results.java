package rokafogo.resultssave;

import lombok.*;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * A végeredmények classa.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Results {

    @NonNull private String playerName1;
    @NonNull private String playerName2;
    private int steps;
    @NonNull private Duration duration;
    @NonNull private ZonedDateTime created;
}
