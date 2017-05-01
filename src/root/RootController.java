package root;

import albums.AlbumsController;
import albums.AlbumsModel;
import albums.AlbumsView;
import artists.ArtistsController;
import artists.ArtistsModel;
import artists.ArtistsView;
import images.ImagesController;
import images.ImagesView;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
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
import java.util.prefs.Preferences;

public class RootController {
    private RootModel rootModel;
    private boolean playlistMode = false;
    private ChangeListener<CategoryType> menuListener = (ov, oldValue, newValue) -> {
		rootModel.setSelectedCategory(newValue);
	};

    public RootController(final RootModel model) {
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

    void togglePlaylist(final RootView rootView) {
        rootModel.togglePlaylistMode();
        rootView.getPlaylistButton().setText(rootModel.isPlaylistMode() ? "Done" : "Create Playlist");
    }

    void chooseDirectory(final Stage stage) {
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

    void toggleDarkMode(final ObservableList<String> stylesheets) {
		rootModel.darkModeProperty().set(!rootModel.darkModeProperty().get());
		final boolean isDarkMode = rootModel.darkModeProperty().get();
		if (isDarkMode) {
			stylesheets.add("root/darkMode.css");
		} else {
			stylesheets.remove("root/darkMode.css");
		}
		Preferences.userNodeForPackage(RootModel.class).putBoolean("DarkMode", rootModel.darkModeProperty().get());
	}

	void shufflePressed() {
		Player.instance().toggleShuffle();
	}
	
	InvalidationListener seek(final Slider seekbar) {
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

	ChangeListener volumeUpdated(final Slider seekbar) {
    	return (observable, oldValue, newValue) -> {
			Player.instance().saveVolume();
		};
    }

	public void volDownPressed() {
		double currentVolume = Player.instance().getVolume();
		if (currentVolume >= 0.0625) {
			Player.instance().setVolume(currentVolume - 0.0625);
		} else {
			Player.instance().setVolume(0);
		}
	}

	public void volUpPressed() {
		double currentVolume = Player.instance().getVolume();
		if (currentVolume <= 0.9375) {
			Player.instance().setVolume(currentVolume + 0.0625);
		} else {
			Player.instance().setVolume(1.00);
		}
	}

	public void updateCategoryView(final RootView view, final CategoryType value) {
		CategoryView newView = null;
		switch (value) {
			case Songs:
				newView = new SongsView(new SongsController());
				((SongsView)newView).setRootModel(rootModel);
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
			case Images:
				newView = new ImagesView(new ImagesController());
				break;
		}
		newView.setListeners(rootModel);
		view.setCenter((Node) newView);
	}

	public void createPlaylist(final RootView rootView, final ListView<CategoryType> menu, final Playlist playlist) {
		menu.getSelectionModel().selectedItemProperty().removeListener(getMenuListener());
		menu.getSelectionModel().select(CategoryType.Playlists);
		menu.getSelectionModel().selectedItemProperty().addListener(getMenuListener());

		PlaylistModel playlistModel = new PlaylistModel();
		playlistModel.setDirectorySelected(rootModel.isDirectorySelected());
		playlistModel.setSelectedPlaylist(playlist);
		CategoryView newView = new PlaylistView(playlistModel, new PlaylistController(playlistModel));
		rootView.setCenter((Node) newView);
	}

	public void searchTextChanged(final String searchText) {
		rootModel.setSearchText(searchText);
	}

	public void showHelp() {
		final Scene scene = new Scene(new Group());

		final String text = "\t\t\t\t\t\tUsing Boomer Tuner:\n\n" +
				"- Import media by choosing a directory with the accepted forms of media*\n" +
				"- Any media imported is stored in your library. You can delete your library\n" +
				"- Change media type by choosing from the menu on the left.\n" +
				"- Search at the top to find specific items in your library\n" +
				"- Create playlists by pressing the \"Create Playlist\" button\n" +
				"\tControl/Shift+Clicking allows you to select any amount of songs.\n" +
				"\n*Accepted extensions: mp3, m4a, mov, mp4, m3u8, m4v, jp(e)g, png";
		final Text textItem = new Text(text);

		final Button okButton = new Button("OK");

		final GridPane grid = new GridPane();
		grid.setVgap(4);
		grid.setHgap(10);
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.add(textItem, 0, 0);
		grid.add(okButton, 1, 0);

		final Group root = (Group) scene.getRoot();
		root.getChildren().add(grid);

		final Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Help Documentation");
		stage.show();


		okButton.setOnAction(e -> {
			stage.close();
		});
	}
}
