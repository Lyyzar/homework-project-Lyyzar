package rokafogo.ui.startgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A start scene megjelenítéséhez szükséges osztály.
 */
public class StartGameApplication extends Application {

    /**
     * Ezzel töltjük be a start fxml-t és tesszük az ablakra.
     *
     * @param stage A window amire tesszük az fxml-t.
     * @throws IOException Lehet, hogy nincs meg a fájl.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/start.fxml"));
        stage.setTitle("Start");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }
}
