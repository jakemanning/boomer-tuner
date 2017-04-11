package utils;

import models.Album;
import models.Playlist;
import models.Song;

public enum CategoryType {
	Songs (Song.class),
	Playlists (Playlist.class),
	Albums (Album.class), Artists, Videos, Images;

	private CategoryType(Class<? extends Category> type) {
		
	}
	
	@Override
	public String toString() {
		return this.name();
	}
}
