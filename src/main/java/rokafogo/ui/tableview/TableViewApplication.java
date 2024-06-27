package rokafogo.ui.tableview;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A tableview azaz az eredménytábla megjelenítéséhez szükséges osztály.
 */
public class TableViewApplication extends Application {


    /**
     * Ezzel töltjük be az eredmenytabla fxml-t és tesszük az ablakra.
     * @param stage A window amire tesszük az fxml-t.
     * @throws IOException Lehet, hogy nincs meg a fájl.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("eredmenytabla.fxml"));
        stage.setTitle("Eredménytábla");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}