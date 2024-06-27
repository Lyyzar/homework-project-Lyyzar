package rokafogo.ui.tableview;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import rokafogo.resultssave.JsonGameResultManager;
import rokafogo.resultssave.Results;
import rokafogo.util.DurationUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Az eredménytábla vezérlő osztálya.
 */
public class TableViewController {
    @FXML
    private TableView<Results> tableView;

    @FXML
    private TableColumn<Results, String> playerName1;

    @FXML
    private TableColumn<Results, String> playerName2;

    @FXML
    private TableColumn<Results, Integer> steps;

    @FXML
    private TableColumn<Results, String> duration;

    @FXML
    private TableColumn<Results, String> created;

    @FXML
    private void initialize() throws IOException {
        playerName1.setCellValueFactory(new PropertyValueFactory<>("playerName1"));
        playerName2.setCellValueFactory(new PropertyValueFactory<>("playerName2"));
        steps.setCellValueFactory(new PropertyValueFactory<>("steps"));
        duration.setCellValueFactory(
                cellData -> {
                    var duration = cellData.getValue().getDuration();
                    return new ReadOnlyStringWrapper(DurationUtil.formatDuration(duration));
                });
        created.setCellValueFactory(
                cellData -> {
                    var dateTime = cellData.getValue().getCreated();
                    var formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
                    return new ReadOnlyStringWrapper(formatter.format(dateTime));
                }
        );
        ObservableList<Results> observableList = FXCollections.observableArrayList();
        observableList.addAll(new JsonGameResultManager(Path.of("results.json")).getBest(10));
        tableView.setItems(observableList);
    }
}
