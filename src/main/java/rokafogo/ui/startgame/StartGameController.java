package rokafogo.ui.startgame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import rokafogo.ui.boardgame.BoardGameController;

import java.io.IOException;

/**
 * A Start scene-hez tartozó controller osztály.
 */
public class StartGameController {

    @FXML
    private TextField player1;
    @FXML
    private TextField player2;
    @FXML
    private Button start;


    @FXML
    private void startGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/sakktabla.fxml"));
        Parent root = fxmlLoader.load();
        BoardGameController controller = fxmlLoader.getController();
        controller.setPlayerName1(player1.getText());
        controller.setPlayerName2(player2.getText());
        Stage stage = (Stage) start.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Rókafogó játék 1.0");
    }
}