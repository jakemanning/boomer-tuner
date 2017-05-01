package root;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Playable;
import models.Playlist;
import models.Song;
import models.Video;
import org.controlsfx.control.textfield.CustomTextField;
import utils.*;

import java.io.IOException;
import java.util.prefs.Preferences;

public class RootView extends BorderPane implements SelectedCategoryListener, PlayerListener {
	private RootModel rootModel;
	private RootController rootController;
	private Button playlist;
	private ListView<CategoryType> menu;
	private CustomTextField searchField;
	private Label clearSearch;
	private Label volDown;
	private Slider volSlider;
	private Label volUp;
	private Label shuffle;
	private Label previous;
	private Label play;
	private Label next;
	private Label loop;
	private final ImageView volDownImage = createScaledImage(Icon.VOLDOWN.image(), 30);
	private final ImageView volUpImage = createScaledImage(Icon.VOLUP.image(), 30);
	private final ImageView shuffleImage = createScaledImage(Icon.SHUFFLE.image(), 40);
    private final ImageView shufflePressedImage = createScaledImage(Icon.SHUFFLE_PRESSED.image(), 40);
	private final ImageView previousImage = createScaledImage(Icon.PREVIOUS.image(), 40);
	private final ImageView pauseImage = createScaledImage(Icon.PAUSE.image(), 40);
	private final ImageView playImage = createScaledImage(Icon.PLAY.image(), 40);
	private final ImageView nextImage = createScaledImage(Icon.NEXT.image(), 40);
	private final ImageView loopImage = createScaledImage(Icon.LOOP.image(), 40);
    private final ImageView loopPressedImage = createScaledImage(Icon.LOOP_PRESSED.image(), 40);
	private ImageView artwork;
	private Text songTitle;
	private Text songLength;
	private Slider seekbar;
	private Text currentTime;
	private BorderPane controlPane;
	private VBox menuVBox;

	public RootView(final RootModel model, final RootController controller) throws IOException {
		rootModel = model;
		rootController = controller;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("root.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.load();
		getStylesheets().add("root/root.css");
		if (rootModel.darkModeProperty().get()) {
			getStylesheets().add("root/darkMode.css");
		}
		rootModel.addSelectedCategoryListener(this);
		Player.instance().addListener(this);
		lookupViews();

		ObservableList<CategoryType> menuList = FXCollections.observableArrayList(CategoryType.Songs,
				CategoryType.Playlists, CategoryType.Albums, CategoryType.Artists, CategoryType.Videos, CategoryType.Images);
		menu.setItems(menuList);

		menu.getSelectionModel().selectedItemProperty().addListener(rootController.getMenuListener());
		menu.getSelectionModel().select(CategoryType.Songs);

		searchField.textProperty().addListener(obs -> rootController.searchTextChanged(searchField.getText()));
		clearSearch.setOnMouseClicked(obs -> searchField.clear());
		clearSearch.visibleProperty().bind(searchField.textProperty().isNotEmpty());
		clearSearch.setCursor(Cursor.HAND);

		setImages();

		shuffle.setOnMouseClicked(e -> rootController.shufflePressed());
		previous.setOnMouseClicked(e -> rootController.previousPressed());
		next.setOnMouseClicked(e -> rootController.nextPressed());
		play.setOnMouseClicked(e -> rootController.playPressed());
		loop.setOnMouseClicked(e -> rootController.loopPressed());

		playlist.setOnAction(e -> rootController.togglePlaylist(this));
		seekbar.valueChangingProperty().addListener(rootController.seek(seekbar));

		volSlider.valueProperty().bindBidirectional(Player.instance().volumeProperty());
		volDown.setOnMouseClicked(e -> rootController.volDownPressed());
		volDownImage.setOnMouseClicked(e -> rootController.volDownPressed());
		volUp.setOnMouseClicked(e -> rootController.volUpPressed());
		volUpImage.setOnMouseClicked(e -> rootController.volUpPressed());
	}

	@SuppressWarnings("unchecked")
	private void lookupViews() {
		playlist = (Button) lookup("#playlist");
		menu = (ListView<CategoryType>) lookup("#menu");
		searchField = (CustomTextField) lookup("#searchField");
		clearSearch = (Label) searchField.getRight();
		volDown = (Label) lookup("#volumeDown");
		volSlider = (Slider) lookup("#volumeSlider");
		volUp = (Label) lookup("#volumeUp");
		shuffle = (Label) lookup("#shuffle");
		previous = (Label) lookup("#previous");
		play = (Label) lookup("#play");
		next = (Label) lookup("#next");
		loop = (Label) lookup("#loop");
		artwork = (ImageView) lookup("#artwork");
		songTitle = (Text) lookup("#songTitle");
		songLength = (Text) lookup("#songLength");
		seekbar = (Slider) lookup("#seekbar");
		currentTime = (Text) lookup("#currentTime");
		menuVBox = (VBox) lookup("#menuVBox");
		controlPane = (BorderPane) lookup("#controlPane");
	}

	private void setImages() {
        Preferences preferences = Preferences.userNodeForPackage(Player.class);

        volDown.setGraphic(volDownImage);
        volUp.setGraphic(volUpImage);
        previous.setGraphic(previousImage);
        play.setGraphic(playImage);
        next.setGraphic(nextImage);

        final boolean isShuffleMode = preferences.getBoolean("ShuffleMode", false);
        shuffle.setGraphic(isShuffleMode ? shufflePressedImage : shuffleImage);

        final boolean isLoopMode = preferences.getBoolean("LoopMode", false);
        loop.setGraphic(isLoopMode ? loopPressedImage: loopImage);

        volSlider.valueChangingProperty().addListener(rootController.volumeUpdated(volSlider));
    }

	public Button getPlaylistButton() {
		return playlist;
	}

	private ImageView createScaledImage(final Image img, final int width) {
		ImageView iView = new ImageView(img);
		iView.setFitWidth(width);
		iView.setPreserveRatio(true);
		return iView;
	}

	public void initializeMenuBar(final Stage stage) {
		final MenuBar menuBar = (MenuBar) lookup("#menuBar");
		final String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Mac")) {
			menuBar.setUseSystemMenuBar(true);
		}

		final Menu file = createFileMenu(stage);
		final Menu controls = createControlsMenu();
		final Menu helpMenu = createHelpMenu();

		menuBar.getMenus().addAll(file, controls, helpMenu);
	}

	private Menu createFileMenu(final Stage stage) {
		final Menu file = new Menu("File");
		final MenuItem open = new MenuItem("Import Media...");
		open.setOnAction(e -> rootController.chooseDirectory(stage));
		open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));

		final MenuItem clear = new MenuItem("Delete Library");
		clear.setOnAction(e -> MediaLibrary.instance().clearLibrary());

		final CheckMenuItem darkMode = new CheckMenuItem("Dark Mode");
		darkMode.setSelected(rootModel.darkModeProperty().get());
		rootModel.darkModeProperty().addListener((obs, old, val) -> darkMode.setSelected(val));
		darkMode.setOnAction(e -> rootController.toggleDarkMode(getStylesheets()));

		file.getItems().addAll(open, clear, darkMode);
		return file;
	}

	private Menu createControlsMenu() {
		final Menu controls = new Menu("Controls");
		final MenuItem play = new MenuItem("Play/Pause");
		play.setOnAction(e -> rootController.playPressed());
		play.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.SHORTCUT_DOWN));

		final MenuItem previous = new MenuItem("Previous");
		previous.setOnAction(e -> rootController.previousPressed());
		previous.setAccelerator(new KeyCodeCombination(KeyCode.LEFT));

		final MenuItem next = new MenuItem("Next");
		next.setOnAction(e -> rootController.nextPressed());
		next.setAccelerator(new KeyCodeCombination(KeyCode.RIGHT));

		final CheckMenuItem shuffle = new CheckMenuItem("Shuffle");
		shuffle.setSelected(Player.instance().shuffleModeProperty().get());
		Player.instance().shuffleModeProperty().addListener((obs, old, val) -> shuffle.setSelected(val));
		shuffle.setOnAction(e -> rootController.shufflePressed());
		shuffle.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));

		final CheckMenuItem loop = new CheckMenuItem("Repeat");
		loop.setSelected(Player.instance().loopModeProperty().get());
		Player.instance().loopModeProperty().addListener((obs, old, val) -> loop.setSelected(val));
		loop.setOnAction(e -> rootController.loopPressed());
		loop.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN));


		final CheckMenuItem crossfade = new CheckMenuItem("Crossfade");
		crossfade.setSelected(Player.instance().crossfadeModeProperty().get());
		Player.instance().crossfadeModeProperty().addListener((obs, old, val) -> crossfade.setSelected(val));
		crossfade.setOnAction(e -> rootController.crossfadePressed());

		final MenuItem lyrics = new MenuItem("Retrieve Lyrics");
		lyrics.setOnAction(e -> rootController.lyricsPressed());
		lyrics.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN));

		controls.getItems().addAll(play, previous, next, shuffle, loop, crossfade, lyrics);
		return controls;
	}

	private Menu createHelpMenu() {
		final Menu helpMenu = new Menu("Help");
		final MenuItem helpItem = new MenuItem("Boomer Tuner Help");
		helpItem.setOnAction(e -> rootController.showHelp());
		helpItem.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.SHORTCUT_DOWN));
		helpMenu.getItems().addAll(helpItem);
		return helpMenu;
	}

	@Override
	public void selectedCategoryChanged(final CategoryType value) {
		rootController.updateCategoryView(this, value);
	}
	@Override
	public void playlistCreated(final Playlist playlist) {
		rootController.createPlaylist(this, menu, playlist);
	}
	@Override
	public void newSong(final Song song) {
		play.setGraphic(pauseImage);
		artwork.setImage(song.getAlbum().getArtwork());
		artwork.setSmooth(true);
		artwork.setCache(true);
		artwork.setCacheHint(CacheHint.QUALITY);
		artwork.setPreserveRatio(true);
		artwork.setFitWidth(100);
		songLength.setText(song.getDuration());

		final StringBuilder title = new StringBuilder(song.getTitle());

		if (song.getArtist() != null) {
			title.append(" - ").append(song.getArtist().getName());
		}
		songTitle.setText(title.toString());
	}

	@Override
	public void newVideo(final Video video) {
		play.setGraphic(pauseImage);
		songLength.setText(video.durationProperty().get());
		songTitle.setText(video.getTitle());
		MediaView videoView = Player.instance().getView();
		setVideoView(videoView);
	}

	private void setVideoView(final MediaView videoView) {
		// FIXME: For Some reason, this is getting called twice
		menu.getSelectionModel().selectedItemProperty().removeListener(rootController.getMenuListener());
		menu.getSelectionModel().select(CategoryType.Videos);
		menu.getSelectionModel().selectedItemProperty().addListener(rootController.getMenuListener());
		Button xButton = new Button("X");
		xButton.setOnAction(e -> {
			rootModel.setSelectedCategory(CategoryType.Videos);
		});

		artwork.setImage(null);
		VBox container = new VBox();
		container.getChildren().add(xButton);
		container.setBackground(new Background(new BackgroundFill(Paint.valueOf("black"), null, null)));
		container.setAlignment(Pos.CENTER);
		double videoWidth = getWidth() - menu.getWidth();
		double videoHeight = getHeight() - controlPane.getHeight() - xButton.getHeight() - menuVBox.getHeight();
		videoView.setFitWidth(videoWidth);
		videoView.setFitHeight(videoHeight);
		videoView.setPreserveRatio(true);
		VBox.setVgrow(videoView, Priority.ALWAYS);
		container.getChildren().add(videoView);
		setCenter(container);

		widthProperty().addListener((o, old, w) -> videoView.setFitWidth(w.doubleValue() - menu.getWidth()));
		heightProperty().addListener((o, old, h) -> videoView.setFitHeight(h.doubleValue() - controlPane.getHeight() - menuVBox.getHeight() - xButton.getHeight()));
	}

	@Override
	public void playingStatusChanged(final CategoryType type, MediaPlayer.Status status) {
		if (status.equals(MediaPlayer.Status.PLAYING)) {
			play.setGraphic(pauseImage);
			if (type.equals(CategoryType.Videos)) {
				setVideoView(Player.instance().getView());
			}
		} else if (status.equals(MediaPlayer.Status.PAUSED)) {
			play.setGraphic(playImage);
		}
	}

	/**
	 * https://docs.oracle.com/javase/8/javafx/media-tutorial/playercontrol.htm#
	 * BABHHBEB)
	 */
	@Override
	public void timeUpdated(final Playable media, final Duration elapsed, final Duration duration) {
		if (media == null || seekbar == null || elapsed == null || duration == null) {
			return;
		}

		Platform.runLater(() -> {
			String currentTimeText = Playable.format(elapsed);
			currentTime.setText(currentTimeText);

			if (!seekbar.isDisabled() && duration.greaterThan(Duration.ZERO) && !seekbar.isValueChanging()) {
				seekbar.setValue(elapsed.toMillis() / duration.toMillis() * 100.0);
			}
		});
	}

	@Override
	public void shuffleModeUpdated(final boolean shuffling) {
		if (shuffling) {
			shuffle.setGraphic(shufflePressedImage);
		} else {
			shuffle.setGraphic(shuffleImage);
		}
	}

	@Override
	public void loopModeUpdated(final boolean looping) {
		if (looping) {
			loop.setGraphic(loopPressedImage);
		} else {
			loop.setGraphic(loopImage);
		}
	}

	@Override
	public void crossfadeModeUpdated(final boolean crossfade) {
	}
}
