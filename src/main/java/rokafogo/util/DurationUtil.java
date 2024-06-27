package rokafogo.util;

import java.time.Duration;

/**
 * A duration-höz szükséges segédosztály.
 */
public class DurationUtil {

    /**
     * Egy Duration típusú elemet Stringgé formázunk.
     * @param duration A duration, amit stringgé formázunk.
     * @return A Stringgé formázott duration-t adja vissza.
     */
    public static String formatDuration(Duration duration) {
        return String.format("%02d:%02d:%02d",
                duration.toHoursPart(),
                duration.toMinutesPart(),
                duration.toSecondsPart());
    }

}
