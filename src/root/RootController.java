package root;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Slider;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utils.CategoryType;
import utils.MediaLibrary;
import utils.Player;

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

    void togglePlaylist() {
        rootModel.togglePlaylistMode();
    }

    void chooseDirectory(Stage stage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory == null) {
            System.out.println("No Directory selected");
        } else {
            Path musicFolder = Paths.get(selectedDirectory.toURI());
			MediaLibrary.instance().importPath(musicFolder);
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
}
