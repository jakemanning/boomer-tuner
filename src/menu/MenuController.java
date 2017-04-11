package menu;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.beans.value.ChangeListener;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utils.CategoryType;
import utils.MediaLibrary;
import utils.MediaPlayer;

/**
 * Created by bryancapps on 4/3/17.
 */
public class MenuController {
    private MenuModel menuModel;
    private boolean playlistMode = false;
    private ChangeListener<CategoryType> menuListener = (ov, oldValue, newValue) -> {
        MediaLibrary.instance().filterOnCategory(newValue);
        menuModel.setSelectedCategory(newValue);
    };

    public MenuController(MenuModel model) {
        menuModel = model;
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
        menuModel.togglePlaylistMode();
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
