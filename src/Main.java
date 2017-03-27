import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import models.Song;

public class Main extends Application {
	private final static Path musicFolder = Paths.get(System.getProperty("user.home"), "Music", "iTunes",
			"iTunes Media", "Music");
	private TableView<Song> table = new TableView<>();
	private ObservableList<Song> items;
	private MediaPlayer mediaPlayer;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
		primaryStage.setTitle("Hello World");

		final VBox vbox = new VBox();
		vbox.setSpacing(5);

		Scene scene = new Scene(vbox, 800, 575);
		primaryStage.setScene(scene);
		primaryStage.show();

		TableColumn<Song, Integer> trackCol = new TableColumn<>("Track");
		trackCol.setPrefWidth(50);
		trackCol.setCellValueFactory(new PropertyValueFactory<Song, Integer>("track"));

		TableColumn<Song, String> titleCol = new TableColumn<>("Title");
		titleCol.setPrefWidth(200);
		titleCol.setCellValueFactory(new PropertyValueFactory<Song, String>("title"));

		TableColumn<Song, String> artistCol = new TableColumn<>("Artist");
		artistCol.setPrefWidth(200);
		artistCol.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));

		TableColumn<Song, String> albumCol = new TableColumn<>("Album");
		albumCol.setPrefWidth(200);
		albumCol.setCellValueFactory(new PropertyValueFactory<Song, String>("album"));

		table.getColumns().addAll(Arrays.asList(trackCol, titleCol, artistCol, albumCol));
		vbox.getChildren().addAll(table);

		loadSongs(table);
	}

	private void loadSongs(TableView<Song> list) {
		items = FXCollections.observableArrayList();

		table.setItems(items);

		try {
			Files.walk(musicFolder, 5)
					.filter(path -> path.toString().toLowerCase().endsWith(".mp3")
							|| path.toString().toLowerCase().endsWith(".m4a"))
					.map(path -> Song.from(path.toUri())).filter(Objects::nonNull).forEachOrdered(items::add);
		} catch (IOException e) {
			e.printStackTrace();
		}

		list.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
			if (mediaPlayer != null)
				mediaPlayer.stop();
			Media media = new Media(newValue.getUri().toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
