import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
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
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import models.Album;
import models.Artist;
import models.MusicLibrary;
import models.Song;
import utils.Category;
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
	private Text songTitle;
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
	private ChangeListener<Song> songListener;
	private ChangeListener<Category> menuListener;

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

		previous.setOnMousePressed(previousPressed());
		next.setOnMousePressed(nextPressed());

		play.setOnMousePressed(playPressed());
		songListener = selectionListener();
		table.getSelectionModel().selectedItemProperty().addListener(songListener);

		ObservableList<Category> menuList = FXCollections.observableArrayList(Category.Songs, Category.Albums,
				Category.Playlists, Category.Artists);
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

	private ChangeListener<Song> selectionListener() {
		return (ov, oldValue, newValue) -> {
			if (newValue == null) {
				return; // If user selects new directory
			}
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}

			Media media = new Media(newValue.uri.toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();

			songTitle.setText(newValue.title);

			play.setImage(Icon.PAUSE.image());
			try {
				BufferedImage image = (BufferedImage) newValue.album.artwork.getImage();
				artwork.setImage(SwingFXUtils.toFXImage(image, null));
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
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
