package playlists;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import models.Playlist;

/**
 * Created by jakemanning on 4/23/17.
 */
public class PlaylistModel {
    private ObjectProperty<Playlist> selectedPlaylist = new SimpleObjectProperty<>();

    public Playlist getSelectedPlaylist() {
        return selectedPlaylist.get();
    }

    public ObjectProperty<Playlist> selectedPlaylistProperty() {
        return selectedPlaylist;
    }

    public void setSelectedPlaylist(Playlist selectedPlaylist) {
        this.selectedPlaylist.set(selectedPlaylist);
    }
}
