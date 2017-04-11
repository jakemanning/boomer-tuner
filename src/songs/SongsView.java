package songs;

import base.CategoryView;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Album;
import models.Artist;
import models.Song;
import root.RootModel;
import utils.MediaLibrary;
import utils.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bryancapps on 4/4/17.
 */
public class SongsView extends TableView<Song> implements CategoryView {
    private SongsController songsController;
    private TableColumn<Song, Integer> trackCol;
    private TableColumn<Song, String> titleCol;
    private TableColumn<Song, Artist> artistCol;
    private TableColumn<Song, Album> albumCol;
    private ChangeListener<Song> songListener = (ov, oldValue, newValue) -> {
        if (newValue == null) {
            return; // If user selects new directory
        }
        if (MediaPlayer.instance().isPlaying()) {
            MediaPlayer.instance().stop();
        }
        MediaPlayer.instance().play(newValue);
    };

    public SongsView(SongsController controller) {
        songsController = controller;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("songs.fxml"));
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        lookupViews();

//        setItems(MediaLibrary.instance());

        setPlaceholder(new Label("Choose a Directory to play music"));
        trackCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getTrack()));
        titleCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getTitle()));
        artistCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getArtist()));
        albumCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getAlbum()));

        getSelectionModel().selectedItemProperty().addListener(songListener);
    }

    private void lookupViews() {
        // wouldn't be surprised it this doesn't work
        trackCol = (TableColumn<Song, Integer>) getVisibleLeafColumn(0);
        titleCol = (TableColumn<Song, String>) getVisibleLeafColumn(1);
        artistCol = (TableColumn<Song, Artist>) getVisibleLeafColumn(2);
        albumCol = (TableColumn<Song, Album>) getVisibleLeafColumn(3);
    }

    public void setMenuModel(RootModel rootModel) {
        rootModel.addPlaylistModeListener(newValue -> playlistModeChanged(newValue));
    }

    public void playlistModeChanged(boolean playlistMode) {
        //TODO: Move this logic to the controller
        if (playlistMode) {
            getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            getSelectionModel().selectedItemProperty().removeListener(songListener);
        } else {
            ObservableList<Song> selectedCells = getSelectionModel().getSelectedItems();
            if (selectedCells.size() > 0) {
                MediaLibrary.instance().addPlaylist(new ArrayList<>(selectedCells));
            }

            getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            getSelectionModel().selectedItemProperty().addListener(songListener);
        }
    }
}
