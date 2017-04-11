package root;

import javafx.beans.value.ChangeListener;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utils.CategoryType;
import utils.MediaPlayer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by bryancapps on 4/3/17.
 */
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
        MediaPlayer.instance().previous();
    }

    void playPressed() {
        //TODO: Consult Jake to see how these should be implemented using our MediaPlayer class
        if (MediaPlayer.instance().isPlaying()) {
            MediaPlayer.instance().pause();
        } else {
            MediaPlayer.instance().play();
        }
    }

    void nextPressed() {
        MediaPlayer.instance().next();
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
//            MediaLibrary.instance().initializeSongs(musicFolder);
        }
    }
}
