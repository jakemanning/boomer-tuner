package root;

import albums.AlbumsController;
import albums.AlbumsModel;
import albums.AlbumsView;
import artists.ArtistsController;
import artists.ArtistsModel;
import artists.ArtistsView;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import models.Playlist;
import playlists.PlaylistController;
import playlists.PlaylistModel;
import playlists.PlaylistView;
import songs.SongsController;
import songs.SongsView;
import utils.CategoryType;
import utils.CategoryView;
import utils.MediaLibrary;
import utils.Player;
import videos.VideosController;
import videos.VideosView;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RootController {
    private RootModel rootModel;
    private boolean playlistMode = false;
    private ChangeListener<CategoryType> menuListener = (ov, oldValue, newValue) -> {
        rootModel.setSelectedCategory(newValue);
    };

    public RootController(RootModel model) {
        rootModel = model;
    }


    public ChangeListener<CategoryType> getMenuListener() {
        return menuListener;
    }

    void previousPressed() {
		Player.instance().previous();
	}

    void playPressed() {
		if (Player.instance().isPlaying()) {
			Player.instance().pause();
		} else {
			Player.instance().resume();
		}
	}

    void nextPressed() {
		Player.instance().next();
	}

    void togglePlaylist(RootView rootView) {
        rootModel.togglePlaylistMode();
        rootView.getPlaylist().setText(rootModel.isPlaylistMode() ? "Done" : "Create Playlist");
    }

    void chooseDirectory(Stage stage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory == null) {
            System.out.println("No Directory selected");
        } else {
            Path musicFolder = Paths.get(selectedDirectory.toURI());
			MediaLibrary.instance().importPath(musicFolder);
			rootModel.setDirectorySelection(true);
		}
    }

	void shufflePressed() {
		Player.instance().toggleShuffle();
	}
	
	InvalidationListener seek(Slider seekbar) {
		return new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				Player.instance().seek(seekbar.getValue() / 100.0);
			}
        };
	}
	void loopPressed() {
		Player.instance().toggleLoop();
	}

	void crossfadePressed() {
		Player.instance().toggleCrossfade();
	}

	public void updateCategoryView(RootView view, CategoryType value) {
		CategoryView newView = null;
		switch (value) {
			case Songs:
				newView = new SongsView(new SongsController());
				break;
			case Playlists:
				PlaylistModel playlistModel = new PlaylistModel();
				newView = new PlaylistView(playlistModel, new PlaylistController(playlistModel));
				break;
			case Albums:
				AlbumsModel albumsModel = new AlbumsModel();
				newView = new AlbumsView(albumsModel, new AlbumsController(albumsModel), rootModel);
				break;
			case Artists:
				ArtistsModel artistModel = new ArtistsModel();
				newView = new ArtistsView(artistModel, new ArtistsController(artistModel), rootModel);
				break;
			case Videos:
				newView = new VideosView(new VideosController());
				break;
		}
		if (newView != null) {
			newView.setRootModel(rootModel);
			view.setCenter((Node) newView);
		}
	}

	public void createPlaylist(RootView rootView, ListView<CategoryType> menu, Playlist playlist) {
		PlaylistModel playlistModel = new PlaylistModel();
		playlistModel.setDirectorySelected(rootModel.isDirectorySelected());
		playlistModel.setSelectedPlaylist(playlist);
		CategoryView newView = new PlaylistView(playlistModel, new PlaylistController(playlistModel));
		rootView.setCenter((Node) newView);
		menu.getSelectionModel().selectedItemProperty().removeListener(getMenuListener());
		menu.getSelectionModel().select(CategoryType.Playlists);
		menu.getSelectionModel().selectedItemProperty().addListener(getMenuListener());
	}
}
