package utils;

import models.*;

public enum CategoryType {
	Songs (Song.class),
	Playlists (Playlist.class),
	Albums(Album.class),
	Artists(Artist.class),
	Videos(Video.class),
	Images(Image.class);

	private CategoryType(Class<? extends Category> type) {
		
	}
	
	@Override
	public String toString() {
		return this.name();
	}
}
