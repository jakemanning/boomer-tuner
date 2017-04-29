package songs;

import models.Song;

import java.util.function.Predicate;

public class SongsController {
	Predicate<Song> searchFilter(String searchText) {
		return song -> {
			String search = searchText.toLowerCase();
			String title = song.getTitle().toLowerCase();
			String artist = song.getArtist().getName().toLowerCase();
			String album = song.getAlbum().getName().toLowerCase();
			return title.contains(search) || artist.contains(search) || album.contains(search);
		};
	}
}
