package menu;

import javafx.beans.value.ChangeListener;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import models.MediaPlayer;
import models.MusicLibrary;
import utils.Category;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by bryancapps on 4/3/17.
 */
public class MenuController {
    private MenuModel menuModel;
    private boolean playlistMode = false;
    private ChangeListener<Category> menuListener = (ov, oldValue, newValue) -> {
        MusicLibrary.instance().filterOnCategory(newValue);
        menuModel.setSelectedCategory(newValue);
    };

    public MenuController(MenuModel model) {
        menuModel = model;
    }


    public ChangeListener<Category> getMenuListener() {
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
            MusicLibrary.instance().initializeSongs(musicFolder);
        }
    }
}
