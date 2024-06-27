package rokafogo.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * A Jackson függőséghez segítő osztály.
 */
public class JacksonHelper {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    /**
     *  Deszirializál objektumok egy listáját JSON-ből.
     * @param in Az input ahonnan a JSON adatokat olvassuk
     * @param elementClass Az elemek osztályát reprezentálja.
     * @return Objektumok listáját adja vissza, ami.
     * @param <T> A lista elemek tipusa.
     * @throws IOException Ha nem található a fájl.
     */
    public static <T> List<T> readList(InputStream in, Class<T> elementClass) throws IOException {
        JavaType type = MAPPER.getTypeFactory().constructCollectionType(List.class, elementClass);
        return MAPPER.readValue(in, type);
    }

    /**
     * Szirializál objektumok egy listáját JSON-be.
     * @param out Az output ahová a JSON adatokat írni akarjuk.
     * @param list Az objektumok egy listája, amiket átakarunk konvertálni.
     * @param <T> A lista elemek típusa.
     * @throws IOException Ha nem található a fájl.
     */
    public static <T> void writeList(OutputStream out, List<T> list) throws IOException {
        MAPPER.writeValue(out, list);
    }

}
