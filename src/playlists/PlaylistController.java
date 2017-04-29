package playlists;

import javafx.beans.value.ChangeListener;
import models.Playlist;

import java.util.function.Predicate;

/**
 * Created by jakemanning on 4/22/17.
 */
public class PlaylistController {
    private final PlaylistModel model;

    public PlaylistController(final PlaylistModel model) {
        this.model = model;
    }

    ChangeListener<? super Playlist> playlistSelectionListener() {
        return (ChangeListener<Playlist>) (observable, oldValue, newValue) -> {
            model.setSelectedPlaylist(newValue);
        };
    }

	Predicate<Playlist> searchFilter(final String searchText) {
		return playlist -> {
			String search = searchText.toLowerCase();
			String name = playlist.getName().toLowerCase();
			return name.contains(search);
		};
	}
}
