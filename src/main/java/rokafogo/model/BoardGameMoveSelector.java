package rokafogo.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.tinylog.Logger;

/**
 * A kiválasztáshoz szükséges osztály.
 */
public class BoardGameMoveSelector {

    /**
     * Egy enum, ami számon tartja, hol tartunk egy lépésnél.
     * A SELECT_FROM a honnant keressük.
     * A SELECT_TO a hovát keressük.
     * A READY_TO_MOVE készek vagyunk lépni.
     */
    public enum Phase {
        SELECT_FROM,
        SELECT_TO,
        READY_TO_MOVE

    }

    private final BoardGameModel model;
    private final ReadOnlyObjectWrapper<Phase> phase = new ReadOnlyObjectWrapper<>(Phase.SELECT_FROM);
    private boolean invalidSelection = false;
    private Coordinate from;
    private Coordinate to;

    /**
     * Az osztály konstruktora.
     *
     * @param model Egy BoardGameModel a paraméter
     */
    public BoardGameMoveSelector(BoardGameModel model) {
        this.model = model;
    }

    /**
     * Megnézzük a phase változónk melyik Phase enum-mal egyenlő.
     *
     * @return Phase enumból ad vissza egyet
     */
    public Phase getPhase() {
        return phase.get();
    }

    /**
     * Ezt használjuk, hogy megnézzük melyik phase-ben vagyunk jelenleg.
     * @return Egy readonlyobjectproperty-t ad vissza.
     */
    public ReadOnlyObjectProperty<Phase> phaseProperty() {
        return phase.getReadOnlyProperty();
    }

    /**
     * Megnézzük, hogy készek vagyunk-e lépni, ha a phase változónk a READY_TO_MOVE-ba van-e.
     *
     * @return boolean-t ad vissza ha true, készek vagyunk lépni, tehát a phase változónk READY_TO_MOVE, ha false akkor nem.
     */
    public boolean isReadyToMove() {
        return phase.get() == Phase.READY_TO_MOVE;
    }

    /**
     * Egy phase változó alapján meghívja a hozzá tarzotó függvényt az adott koordinátával.
     *
     * @param coordinate Az adott koorináta.
     */
    public void select(Coordinate coordinate) {
        switch (phase.get()) {
            case SELECT_FROM -> selectFrom(coordinate);
            case SELECT_TO -> selectTo(coordinate);
            case READY_TO_MOVE -> throw new IllegalStateException();
        }
    }

    private void selectFrom(Coordinate coordinate) {
        var dx = coordinate.getX();
        var dy = coordinate.getY();
        if (selectFromIf(coordinate, dx, dy)) {

            from = coordinate;
            phase.set(Phase.SELECT_TO);
            invalidSelection = false;
        } else {
            invalidSelection = true;
            switch (selectFromLog(coordinate, dx, dy)) {
                case "empty": {
                    Logger.info("It is an empty field, choose a pawn to move it.");
                    break;
                }
                case "!move": {
                    Logger.warn("This pawn can't move elsewhere!");
                    break;
                }
                case "!turn": {
                    Logger.warn("It is not that pawns turn.");
                    break;
                }
            }
        }
    }

    private boolean selectFromIf(Coordinate coordinate, int dx, int dy) {
        var selectedPawn = model.getPawn(coordinate);
        return !model.isEmpty(coordinate) && ((model.turn && selectedPawn == Pawn.ONE) || (!model.turn && selectedPawn == Pawn.MULTIPLE &&
                (model.isEmpty(new Coordinate(dx - 1, dy - 1)) || model.isEmpty(new Coordinate(dx - 1, dy + 1)))));
    }

    private String selectFromLog(Coordinate coordinate, int dx, int dy) {
        String message;
        var selectedPawn = model.getPawn(coordinate);
        if (selectedPawn == Pawn.NONE) {
            message = "empty";
        } else if ((!model.turn && selectedPawn == Pawn.MULTIPLE &&
                (!model.isEmpty(new Coordinate(dx - 1, dy - 1)) || !model.isEmpty(new Coordinate(dx - 1, dy + 1))))) {
            message = "!move";
        } else message = "!turn";
        return message;
    }

    private void selectTo(Coordinate coordinate) {
        if (model.canMove(from, coordinate)) {
            to = coordinate;
            phase.set(Phase.READY_TO_MOVE);
            invalidSelection = false;
        } else {
            Logger.warn("Invalid move.");
            invalidSelection = true;
        }

    }

    /**
     * Elkéri a from változó értékét, ha a phase változónk nem a SELECT_FROM állapotba van.
     *
     * @return A from változóban található koordinátát adja vissza.
     */
    public Coordinate getFrom() {
        if (phase.get() == Phase.SELECT_FROM) {
            throw new IllegalStateException();
        }
        return from;
    }

    /**
     * Elkéri a to változó értékét, ha a phase változónk a READY_TO_MOVE állapotba van.
     *
     * @return A to változóban található koordinátát adja vissza.
     */
    public Coordinate getTo() {
        if (phase.get() != Phase.READY_TO_MOVE) {
            throw new IllegalStateException();
        }
        return to;
    }

    /**
     * Ha a phase változónk READY_TO_MOVE akkor meghívja a modellünk move metódusát.
     * Különben exception-t dob.
     */
    public void makeMove() {
        if (phase.get() != Phase.READY_TO_MOVE) {
            throw new IllegalStateException();
        }
        model.move(from, to);
        reset();
    }

    private void reset() {
        from = null;
        to = null;
        phase.set(Phase.SELECT_FROM);
        invalidSelection = false;
    }

}


