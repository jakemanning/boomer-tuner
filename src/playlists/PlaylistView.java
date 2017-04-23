package playlists;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import models.Playlist;
import models.Song;
import root.RootModel;
import songs.SongsController;
import songs.SongsView;
import utils.CategoryView;
import utils.MediaLibrary;

/**
 * Created by jakemanning on 4/22/17.
 */
public class PlaylistView extends SplitPane implements CategoryView {

    private final PlaylistModel model;
    private final PlaylistController controller;
    private ListView<Playlist> playlists;
    private SongsView detail;

    public PlaylistView(final PlaylistModel model, final PlaylistController controller) {
        this.model = model;
        this.controller = controller;
        initializeViews();

        initializeArtists();
        initializeDetailView();
    }

    private void initializeArtists() {
        playlists.setItems(MediaLibrary.instance().getPlaylists());
        playlists.getSelectionModel().selectedItemProperty().addListener(controller.playlistSelectionListener());
    }

    private void initializeDetailView() {
        detail.setPlaceholder(new Label("Choose a directory to view playlists"));
        detail.getColumns().remove(2); // remove artist column

        model.selectedPlaylistProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue.getItems());
            ObservableList<Song> items = FXCollections.observableArrayList();
            newValue.getItems().forEach(playable -> {
                items.add((Song)playable);
            });
            detail.setItems(items);
        });
        if (model.getSelectedPlaylist() == null) {
            detail.setItems(null);
        }
    }

    private void initializeViews() {
        setPrefHeight(575);
        setPrefWidth(668);
        playlists = new ListView<>();
        detail = new SongsView(new SongsController());
        getItems().addAll(playlists, detail);
        setDividerPositions(0.28f);
    }

    @Override
    public void setRootModel(RootModel rootModel) {
        rootModel.setPlaylistModeListener(this::playlistModeChanged);
    }

    private void playlistModeChanged(boolean playlistMode) {
        detail.playlistModeChanged(playlistMode);
    }
}
