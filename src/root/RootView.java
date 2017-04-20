package root;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Song;
import models.Video;
import songs.SongsController;
import songs.SongsView;
import utils.*;
import videos.VideosController;
import videos.VideosView;

import java.io.IOException;

public class RootView extends BorderPane implements SelectedCategoryListener, PlayerListener {
	private RootModel rootModel;
    private RootController rootController;
    private Button playlist;
    private ListView<CategoryType> menu;
    private Label shuffle;
    private Label previous;
    private Label play;
    private Label next;
    private Label loop;
	private final ImageView shuffleImage = createScaledImage(Icon.SHUFFLE.image());
	private final ImageView shufflePressedImage = createScaledImage(Icon.SHUFFLE_PRESSED.image());
    private final ImageView previousImage = createScaledImage(Icon.PREVIOUS.image());
    private final ImageView pauseImage = createScaledImage(Icon.PAUSE.image());
	private final ImageView playImage = createScaledImage(Icon.PLAY.image());
	private final ImageView nextImage = createScaledImage(Icon.NEXT.image());
	private final ImageView loopPressedImage = createScaledImage(Icon.LOOP.image());
	private final ImageView loopImage = createScaledImage(Icon.LOOP_PRESSED.image());
	private ImageView artwork;
	private Text songTitle;
	private Text songLength;
	private ToggleButton shuffleButton;

    public RootView(RootModel model, RootController controller) throws IOException {
        rootModel = model;
        rootController = controller;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("root.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.load();
        rootModel.addSelectedCategoryListener(this);
		Player.instance().addListener(this);

        lookupViews();

        ObservableList<CategoryType> menuList = FXCollections.observableArrayList(CategoryType.Songs, CategoryType.Albums,
        		CategoryType.Playlists, CategoryType.Artists, CategoryType.Videos);
        menu.setItems(menuList);

        menu.getSelectionModel().selectedItemProperty().addListener(rootController.getMenuListener());
		menu.getSelectionModel().select(CategoryType.Songs);

		shuffle.setGraphic(shuffleImage);
        previous.setGraphic(previousImage);
        play.setGraphic(playImage);
        next.setGraphic(nextImage);
        loop.setGraphic(loopImage);

        previous.setOnMousePressed(e -> rootController.previousPressed());
        next.setOnMousePressed(e -> rootController.nextPressed());
        play.setOnMousePressed(e -> rootController.playPressed());

        playlist.setOnAction(e -> rootController.togglePlaylist());
		shuffleButton.setOnAction(e -> rootController.shuffle());
	}

	@SuppressWarnings("unchecked")
	private void lookupViews() {
		playlist = (Button) lookup("#playlist");
		menu = (ListView<CategoryType>) lookup("#menu");
		shuffle = (Label) lookup("#shuffle");
		previous = (Label) lookup("#previous");
		play = (Label) lookup("#play");
		next = (Label) lookup("#next");
		loop = (Label) lookup("#loop");
		artwork = (ImageView) lookup("#artwork");
		songTitle = (Text) lookup("#songTitle");
		songLength = (Text) lookup("#songLength");
		shuffleButton = (ToggleButton) lookup("#shuffleButton");
	}

	private ImageView createScaledImage(Image img){
		ImageView iView = new ImageView(img);
		iView.setFitWidth(40);
		iView.setPreserveRatio(true);
		return iView;
	}
    public void initializeMenuBar(final Stage stage) {
        final MenuBar menuBar = new MenuBar();
        final String os = System.getProperty("os.name");
        if (os != null && os.startsWith("Mac")) {
            menuBar.setUseSystemMenuBar(true);
        }

        final Menu file = new Menu("File");
        final MenuItem open = new MenuItem("Import Media...");
        open.setOnAction(e -> rootController.chooseDirectory(stage));
        open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
        file.getItems().add(open);
        menuBar.getMenus().add(file);
        setTop(menuBar);
    }

    @Override
    public void selectedCategoryChanged(CategoryType value) {
        CategoryView newView = null;
        switch (value) {
            case Songs:
                newView = new SongsView(new SongsController());
                break;
            case Playlists:
                break;
            case Albums:
                break;
            case Artists:
                break;
            case Videos:
				newView = new VideosView(new VideosController());
				break;
		}
        if (newView != null) {
			newView.setRootModel(rootModel);
			setCenter((Node) newView);
		}
    }

	@Override
	public void newSong(Song song) {
		play.setGraphic(pauseImage);
		artwork.setImage(song.getAlbum().getArtwork());
		songLength.setText(song.getDuration());
		songTitle.setText(song.getTitle());
	}

	@Override
	public void newVideo(Video video) {
		play.setGraphic(pauseImage);
		songLength.setText(video.getDuration());
		songTitle.setText(video.getTitle());
		MediaView videoView = Player.instance().getView();
		setVideoView(videoView);
	}

	private void setVideoView(MediaView videoView) {
		artwork.setImage(null);
		videoView.fitWidthProperty().bind(Bindings.selectDouble(videoView.sceneProperty(), "width"));
		videoView.fitHeightProperty().bind(Bindings.selectDouble(videoView.sceneProperty(), "height"));
		videoView.setPreserveRatio(true);
		setCenter(videoView);
	}

	@Override
	public void playingStatusChanged(CategoryType type, MediaPlayer.Status status) {
		if (status.equals(MediaPlayer.Status.PLAYING)) {
			play.setGraphic(pauseImage);
			if (type.equals(CategoryType.Videos)) {
				setVideoView(Player.instance().getView());
			}
		} else if (status.equals(MediaPlayer.Status.PAUSED)) {
			play.setGraphic(playImage);
		}
	}
}
