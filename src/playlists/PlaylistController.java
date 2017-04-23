package playlists;

import javafx.beans.value.ChangeListener;
import models.Playlist;

/**
 * Created by jakemanning on 4/22/17.
 */
public class PlaylistController {
    private final PlaylistModel model;

    public PlaylistController(PlaylistModel model) {
        this.model = model;
    }

    ChangeListener<? super Playlist> playlistSelectionListener() {
        return (ChangeListener<Playlist>) (observable, oldValue, newValue) -> {
            model.setSelectedPlaylist(newValue);
        };
    }
}
