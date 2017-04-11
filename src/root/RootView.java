package root;

import base.CategoryView;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Song;
import songs.SongsController;
import songs.SongsView;
import utils.CategoryType;
import utils.Icon;
import utils.MusicPlayer;
import utils.MusicPlayerListener;
import videos.VideosView;

import java.io.IOException;

/**
 * Created by bryancapps on 4/4/17.
 */
public class RootView extends BorderPane implements SelectedCategoryListener, MusicPlayerListener {
	private RootModel rootModel;
    private RootController rootController;
    private Button playlist;
    private ListView<CategoryType> menu;
    private ImageView previous;
    private ImageView play;
    private ImageView next;
	private ImageView artwork;
	private Text songTitle;
	private Text songLength;

    public RootView(RootModel model, RootController controller) throws IOException {
        rootModel = model;
        rootController = controller;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("root.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.load();
        rootModel.addSelectedCategoryListener(this);
		MusicPlayer.instance().addListener(this);

        lookupViews();

        ObservableList<CategoryType> menuList = FXCollections.observableArrayList(CategoryType.Songs, CategoryType.Albums,
        		CategoryType.Playlists, CategoryType.Artists, CategoryType.Videos);
        menu.setItems(menuList);

        menu.getSelectionModel().selectedItemProperty().addListener(rootController.getMenuListener());
        menu.getSelectionModel().select(CategoryType.Songs); // does this work? I want it to select in sidebar and show in center

        previous.setImage(Icon.PREVIOUS.image());
        play.setImage(Icon.PLAY.image());
        next.setImage(Icon.NEXT.image());

        previous.setOnMousePressed(e -> rootController.previousPressed());
        next.setOnMousePressed(e -> rootController.nextPressed());
        play.setOnMousePressed(e -> rootController.playPressed());

        playlist.setOnAction(e -> rootController.togglePlaylist());
    }

    private void lookupViews() {
        playlist = (Button) lookup("#playlist");
        menu = (ListView<CategoryType>) lookup("#menu");
        previous = (ImageView) lookup("#previous");
        play = (ImageView) lookup("#play");
        next = (ImageView) lookup("#next");
		artwork = (ImageView) lookup("#artwork");
		songTitle = (Text) lookup("#songTitle");
		songLength = (Text) lookup("#songLength");
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
                newView = new VideosView();
                break;
        }
        if (newView != null) {
            newView.setMenuModel(rootModel);
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
	public void playingStatusChanged(MediaPlayer.Status status) {
		if (status.equals(MediaPlayer.Status.PLAYING)) {
			play.setImage(Icon.PAUSE.image());
		} else if (status.equals(MediaPlayer.Status.PAUSED)) {
			play.setImage(Icon.PLAY.image());
		}
	}
}
