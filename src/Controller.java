import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import models.Album;
import models.Artist;
import models.Song;
import utils.Icon;

public class Controller {
	@FXML
	private TableView<Song> table;
	@FXML
	private TableColumn<Song, Integer> trackCol;
	@FXML
	private TableColumn<Song, String> titleCol;
	@FXML
	private TableColumn<Song, Artist> artistCol;
	@FXML
	private TableColumn<Song, Album> albumCol;
	@FXML
	private ImageView artwork;
	@FXML
	private ImageView previous;
	@FXML
	private ImageView play;
	@FXML
	private ImageView next;

	private static Path musicFolder = Paths.get(System.getProperty("user.home"), "Music", "iTunes", "iTunes Media",
			"Music");
	private MediaPlayer mediaPlayer;

	@FXML
	private void initialize() {

		table.setPlaceholder(new Label("Choose a Directory to play music"));
		trackCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().track));
		titleCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().title));
		artistCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().artist));
		albumCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().album));

		previous.setImage(Icon.PREVIOUS.image());
		play.setImage(Icon.PLAY.image());
		next.setImage(Icon.NEXT.image());

		// Need to move these into methods
		previous.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				table.getSelectionModel().selectAboveCell();
			}
		});

		next.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				table.getSelectionModel().selectBelowCell();
			}
		});

		play.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mediaPlayer == null) {
					if (!table.getItems().isEmpty()) {
						table.getSelectionModel().select(0);
					}
					return;
				}
				
				Status status = mediaPlayer.getStatus();

				if (status == Status.PLAYING) {
					mediaPlayer.pause();
					play.setImage(Icon.PLAY.image());
				} else if (status == Status.PAUSED) {
					final int current = table.getSelectionModel().getSelectedIndex();
					table.getSelectionModel().clearSelection();
					table.getSelectionModel().select(current);
				}
			}
		});

		addListener();
	}

	public void initializeMenuBar(final Stage primaryStage, final BorderPane root) {
		final MenuBar menuBar = new MenuBar();
		final String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Mac")) {
			menuBar.setUseSystemMenuBar(true);
		}

		final Menu file = new Menu("File");
		final MenuItem open = new MenuItem("Open");
		open.setOnAction((e) -> {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			File selectedDirectory = directoryChooser.showDialog(primaryStage);

			if (selectedDirectory == null) {
				System.out.println("No Directory selected");
			} else {
				musicFolder = Paths.get(selectedDirectory.toURI());
				loadSongs();
			}
		});
		open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
		file.getItems().add(open);
		menuBar.getMenus().add(file);
		root.setTop(menuBar);
	}

	private void loadSongs() {
		ObservableList<Song> items = FXCollections.observableArrayList();
		table.setItems(items);

		try {
			Files.walk(musicFolder, 5)
					.filter(path -> path.toString().toLowerCase().endsWith(".mp3")
							|| path.toString().toLowerCase().endsWith(".m4a"))
					.map(path -> Song.from(path.toUri())).filter(Objects::nonNull).forEach(items::add);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addListener() {
		table.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
			if (newValue == null) {
				return; // If user selects new directory
			}
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}

			Media media = new Media(newValue.uri.toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();

			play.setImage(Icon.PAUSE.image());
			try {
				BufferedImage image = (BufferedImage) newValue.artwork.getImage();
				artwork.setImage(SwingFXUtils.toFXImage(image, null));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
