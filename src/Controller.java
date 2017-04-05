import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import models.Album;
import models.Artist;
import models.MusicLibrary;
import models.Song;
import utils.Category;
import utils.Icon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Not used, see MenuController in the menu package and SongsController in the songs package.
 * Will be removed in the future once those are fully functional.
 */
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
	private Text songTitle;
	@FXML
	private Text songLength;
	@FXML
	private ImageView artwork;
	@FXML
	private ImageView previous;
	@FXML
	private ImageView play;
	@FXML
	private ImageView next;
	@FXML
	private ListView<Category> menu;
	@FXML
	private Button playlist;

	private boolean playlistMode = false;
	private MediaPlayer mediaPlayer;
	private ChangeListener<Song> songListener = (ov, oldValue, newValue) -> {
		if (newValue == null) {
			return; // If user selects new directory
		}
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
		playNewSong(newValue);
	};
	private ChangeListener<Category> menuListener;

	@FXML
	private void initialize() {
		table.setPlaceholder(new Label("Choose a Directory to play music")); // this stuff obv not added to MenuView
		trackCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getTrack()));
		titleCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getTitle()));
		artistCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getArtist()));
		albumCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getAlbum()));

		previous.setImage(Icon.PREVIOUS.image());
		play.setImage(Icon.PLAY.image());
		next.setImage(Icon.NEXT.image());

		previous.setOnMousePressed(previousPressed());
		next.setOnMousePressed(nextPressed());

		play.setOnMousePressed(playPressed());
		table.getSelectionModel().selectedItemProperty().addListener(songListener); // not added to MenuView

		ObservableList<Category> menuList = FXCollections.observableArrayList(Category.Songs, Category.Albums,
				Category.Playlists, Category.Artists, Category.Videos);
		menu.setItems(menuList);
		menuListener = menuListener();
		menu.getSelectionModel().selectedItemProperty().addListener(menuListener);
	}

	@FXML
	private void togglePlaylist() {
		playlistMode = !playlistMode;

		if (playlistMode) {
			table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			table.getSelectionModel().selectedItemProperty().removeListener(songListener);
			playlist.setText("Done");
		} else {
			ObservableList<Song> selectedCells = table.getSelectionModel().getSelectedItems();
			if (selectedCells.size() > 0) {
				MusicLibrary.instance().addPlaylist(selectedCells.stream().collect(Collectors.toList()));
			}

			playlist.setText("Create Playlist");
			table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			table.getSelectionModel().selectedItemProperty().addListener(songListener);
		}
	}

	public void initializeMenuBar(final Stage stage, final BorderPane root) {
		final MenuBar menuBar = new MenuBar();
		final String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Mac")) {
			menuBar.setUseSystemMenuBar(true);
		}

		final Menu file = new Menu("File");
		final MenuItem open = new MenuItem("Open");
		open.setOnAction(chooseDirectory(stage));
		open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
		file.getItems().add(open);
		menuBar.getMenus().add(file);
		root.setTop(menuBar);
	}

	private void loadSongs(Path musicFolder) {
		table.setItems(MusicLibrary.instance().initializeSongs(musicFolder));
	}


	private void playNewSong(Song newValue) {
		Media media = new Media(newValue.getUri().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();

		songTitle.setText(newValue.getTitle());

		play.setImage(Icon.PAUSE.image());
		try {
			BufferedImage image = (BufferedImage) newValue.getAlbum().getArtwork().getImage();
			artwork.setImage(SwingFXUtils.toFXImage(image, null));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ChangeListener<Category> menuListener() {
		return (ov, oldValue, newValue) -> {
			MusicLibrary.instance().filterOnCategory(newValue);
		};
	}

	private EventHandler<MouseEvent> previousPressed() {
		return (e) -> {
			table.getSelectionModel().selectAboveCell();
		};
	}

	private EventHandler<MouseEvent> nextPressed() {
		return (e) -> {
			table.getSelectionModel().selectBelowCell();
		};
	}

	private EventHandler<MouseEvent> playPressed() {
		return (e) -> {
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
		};
	}

	private EventHandler<ActionEvent> chooseDirectory(Stage stage) {
		return (e) -> {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			File selectedDirectory = directoryChooser.showDialog(stage);

			if (selectedDirectory == null) {
				System.out.println("No Directory selected");
			} else {
				Path musicFolder = Paths.get(selectedDirectory.toURI());
				loadSongs(musicFolder);
			}
		};
	}
}
