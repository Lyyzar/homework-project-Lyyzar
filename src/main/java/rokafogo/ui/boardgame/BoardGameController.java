package rokafogo.ui.boardgame;


import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rokafogo.resultssave.GameResultManager;
import rokafogo.resultssave.JsonGameResultManager;
import rokafogo.model.BoardGameMoveSelector;
import rokafogo.resultssave.Results;
import rokafogo.model.BoardGameModel;
import rokafogo.model.Coordinate;
import org.tinylog.Logger;
import rokafogo.util.Stopwatch;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.time.ZonedDateTime;


/**
 * Ez a játék controller osztálya.
 */
public class BoardGameController {

    @FXML
    private GridPane board;
    private int steps = 0;
    private final BoardGameModel model = new BoardGameModel();
    private final Stopwatch stopwatch = new Stopwatch();
    private final BoardGameMoveSelector selector = new BoardGameMoveSelector(model);
    private ZonedDateTime created = null;

    private String playerName1 = "";
    private String playerName2 = "";

    @FXML
    private void initialize() {
        board.getChildren().remove(0);
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                if (!((i + j) % 2 == 0)) {
                    square.setStyle("-fx-background-color:GRAY");
                }
                board.add(square, j, i);
            }
        }
        stopwatch.start();
        created = ZonedDateTime.now();
        selector.phaseProperty().addListener(this::showSelectionPhaseChange);
    }


    private StackPane createSquare(int i, int j) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.setPrefHeight(62.5);
        square.setPrefWidth(62.5);
        String filename = "";
        switch (model.getPawn(new Coordinate(i, j))) {
            case ONE -> {
                filename = "white-pawn.png";
            }
            case MULTIPLE -> {
                filename = "black-pawn.png";
            }
            case NONE -> {
                filename = "none.png";
            }
        }
        square.getChildren().add(setImage(filename));
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }


    private ImageView setImage(String filename) {
        var imageView = new ImageView();
        imageView.setFitWidth(40);
        imageView.setFitHeight(50);
        String resourcePath = String.format("images/%s", filename);
        Image image = new Image(getClass().getClassLoader().getResourceAsStream(resourcePath));
        imageView.setImage(image);
        return imageView;
    }


    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        Logger.info("Click on square ({},{})", row, col);
        selector.select(new Coordinate(row, col));
        if (selector.isReadyToMove()) {
            switch (model.getPawn(selector.getFrom())) {
                case ONE -> {
                    changePictures(1);
                }
                case MULTIPLE -> {
                    changePictures(2);
                }
            }
            selector.makeMove();
            steps = steps + 1;
            if (model.isGoal()) {
                stopwatch.stop();
                String winner = "";
                var alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("A játéknak vége!");
                if (model.turn) {
                    winner = playerName2;
                } else winner = playerName1;
                alert.setContentText(String.format("%s nyerte a játékot!", winner));
                alert.showAndWait();
                try {
                    saveState();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/eredmenytabla.fxml"));
                    Stage stage = (Stage) board.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void saveState() throws IOException {
        var state = Results.builder()
                .playerName1(playerName1)
                .playerName2(playerName2)
                .steps(steps)
                .duration(Duration.ofSeconds(stopwatch.secondsProperty().get()))
                .created(created)
                .build();
        GameResultManager manager = new JsonGameResultManager(Path.of("results.json"));
        manager.add(state);

    }

    private void changePictures(int i) {
        getSquare(selector.getFrom()).getChildren().remove(0);
        getSquare(selector.getFrom()).getChildren().add(setImage("none.png"));
        getSquare(selector.getTo()).getChildren().remove(0);
        switch (i) {
            case 1 -> {
                getSquare(selector.getTo()).getChildren().add(setImage("white-pawn.png"));
            }
            case 2 -> {
                getSquare(selector.getTo()).getChildren().add(setImage("black-pawn.png"));
            }
        }
    }


    private void showSelectionPhaseChange(ObservableValue<? extends BoardGameMoveSelector.Phase> value, BoardGameMoveSelector.Phase oldPhase, BoardGameMoveSelector.Phase newPhase) {
        switch (newPhase) {
            case SELECT_FROM -> {
            }
            case SELECT_TO -> showSelection(selector.getFrom());
            case READY_TO_MOVE -> hideSelection(selector.getFrom());
        }
    }


    private void showSelection(Coordinate coordinate) {
        var square = getSquare(coordinate);
        square.getStyleClass().add("selected");
    }


    private void hideSelection(Coordinate coordinate) {
        var square = getSquare(coordinate);
        square.getStyleClass().remove("selected");
    }

    /**
     * Beállítja az 1.player nevét.
     *
     * @param name a név amit beakarunk állítani a játékosnak
     */
    public void setPlayerName1(String name) {
        this.playerName1 = name;
    }

    /**
     * Beállítja az 2.player nevét.
     *
     * @param name a név amit beakarunk állítani a játékosnak
     */
    public void setPlayerName2(String name) {
        this.playerName2 = name;
    }

    private StackPane getSquare(Coordinate coordinate) {
        for (var child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == coordinate.getX() && GridPane.getColumnIndex(child) == coordinate.getY()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }
}