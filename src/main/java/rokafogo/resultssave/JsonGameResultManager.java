package rokafogo.resultssave;


import lombok.NonNull;
import rokafogo.util.JacksonHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * A játék végeredményéhez szükséges függvények implementálása.
 */
public class JsonGameResultManager implements GameResultManager {

    private Path filePath;

    /**
     * Beállítom a managernek a file-om path-ját.
     * @param filePath A fájlom path-ja.
     */
    public JsonGameResultManager(@NonNull Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Hozzá adunk a manager result listájához egy újat.
     *
     * @param result A result amit hozzá akarunk adni.
     * @return Vissza adja az eddigi resultok listáját kiegészítve az újjal.
     * @throws IOException
     */
    @Override
    public List<Results> add(@NonNull Results result) throws IOException {
        var results = getAll();
        results.add(result);
        try (var out = Files.newOutputStream(filePath)) {
            JacksonHelper.writeList(out, results);
        }
        return results;
    }

    /**
     * Az összes eddigi resultot kiolvassa a fájlból egy listába.
     *
     * @return Ha nem létezik a fájl létrehoz neki egy listát, vissza adja a fájlt tartalmát listában.
     * @throws IOException
     */
    public List<Results> getAll() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<Results>();
        }
        try (var in = Files.newInputStream(filePath)) {
            return JacksonHelper.readList(in, Results.class);
        }
    }

}
