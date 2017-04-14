package root;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
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
    private ImageView shuffle;
    private ImageView previous;
    private ImageView play;
    private ImageView next;
    private ImageView loop;
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

		shuffle.setImage(Icon.SHUFFLE.image());
        previous.setImage(Icon.PREVIOUS.image());
        play.setImage(Icon.PLAY.image());
        next.setImage(Icon.NEXT.image());
        loop.setImage(Icon.LOOP.image());

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
		shuffle = (ImageView) lookup("#shuffle");
		previous = (ImageView) lookup("#previous");
		play = (ImageView) lookup("#play");
		next = (ImageView) lookup("#next");
		loop = (ImageView) lookup("#loop");
		artwork = (ImageView) lookup("#artwork");
		songTitle = (Text) lookup("#songTitle");
		songLength = (Text) lookup("#songLength");
		shuffleButton = (ToggleButton) lookup("#shuffleButton");
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
		play.setImage(Icon.PAUSE.image());
		artwork.setImage(song.getAlbum().getArtwork());
		songLength.setText(song.getDuration());
		songTitle.setText(song.getTitle());
	}

	@Override
	public void newVideo(Video video) {
		play.setImage(Icon.PAUSE.image());
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
			play.setImage(Icon.PAUSE.image());
			if (type.equals(CategoryType.Videos)) {
				setVideoView(Player.instance().getView());
			}
		} else if (status.equals(MediaPlayer.Status.PAUSED)) {
			play.setImage(Icon.PLAY.image());
		}
	}
}
