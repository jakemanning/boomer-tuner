package videos;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Video;

public class VideosController {
    @FXML
    private TableView<Video> table;
    @FXML
    private TableColumn<Video, String> titleCol;
    @FXML
    private TableColumn<Video, String> durationCol;

    @FXML
    private void initialize() {

    }
}