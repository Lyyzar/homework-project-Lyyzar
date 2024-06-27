package rokafogo.resultssave;


import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * A játék végeredményeinek előállításához szükséges függvények.
 */

public interface GameResultManager {

    /**
     * Hozzá adunk egy resultot egy result listához.
     * @param result A hozzá adandó result.
     * @return Resultok listáját adja vissza.
     * @throws IOException Ha nincs meg a fájl.
     */
    List<Results> add(Results result) throws IOException;

    /**
     * Az összes eddigi resultot kiolvassa a fájlból egy listába.
     * @return Ha nem létezik a fájl létrehoz neki egy listát, vissza adja a fájlt tartalmát listában.
     * @throws IOException Ha nincs meg a fájl.
     */
    List<Results> getAll() throws IOException;

    /**
     * Vissza adja a resultok egy listájából a valamennyi lejobbat.
     * @param limit Az érték, hogy mennyi legjobbat addjon vissza.
     * @return Vissza adja a listát leszűrve a valamennyi legjobbra.
     * @throws IOException Ha nincs meg a fájl.
     */
    default List<Results> getBest(int limit) throws IOException {
        return getAll()
                .stream()
                .sorted(Comparator.comparingInt(Results::getSteps))
                .limit(limit)
                .toList();
    }

}

