package utils;

import java.nio.file.Path;

import models.Song;
import models.Video;

public interface Category {
	public static <T extends Category> Category from(final Path path, Class<T> type) {
		if(type == Song.class) {
			
		} else if (type == Playlist.class) {
			
		} else if (type == Album.class) {
			
		} else if (type == Artist.class) {
			
		} else if {
			
		}
		switch (type) {
		case Songs: return Song.from(path.toUri());
		case Playlists:
		case Albums:
		case Artists:
		case Videos: return Video.from(path.toUri());
		case Images:
		default:
			return null;
		}
	}
}
