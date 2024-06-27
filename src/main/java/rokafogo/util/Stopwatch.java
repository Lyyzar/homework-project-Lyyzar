package rokafogo.util;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 * A stopwatchunk osztálya.
 */
public class Stopwatch {

    private LongProperty seconds = new SimpleLongProperty();
    private StringProperty hhmmss = new SimpleStringProperty();

    private Timeline timeline;

    /**
     * A stopwatchunk konstruktora.
     */
    public Stopwatch() {
        timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> seconds.set(seconds.get() + 1)),
                new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        hhmmss.bind(Bindings.createStringBinding(() -> DurationFormatUtils.formatDuration(seconds.get() * 1000, "HH:mm:ss"), seconds));
    }

    /**
     * Vissza adja a stopwatch által mért időt másodpercben.
     * @return A mért idő.
     */
    public LongProperty secondsProperty() {
        return seconds;
    }


    /**
     * Elindítja a stopwatchot.
     */
    public void start() {
        timeline.play();
    }

    /**
     * Megállítja a stopwatchot.
     */
    public void stop() {
        timeline.pause();
    }

    /**
     * Reseteli a stopwatch-ot.
     */
    public void reset() {
        if (timeline.getStatus() != Animation.Status.PAUSED) {
            throw new IllegalStateException();
        }
        seconds.set(0);
    }

}