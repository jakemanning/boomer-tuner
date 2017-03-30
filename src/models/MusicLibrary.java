package models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Category;

public class MusicLibrary {
	private static MusicLibrary instance = new MusicLibrary();
	private ObservableList<Song> items = FXCollections.observableArrayList();
	public List<Song> songs = new ArrayList<>();
	public List<Album> albums = new ArrayList<>();
	public List<Artist> artists = new ArrayList<>();
	public List<Playlist> playlists = new ArrayList<>();

	public static MusicLibrary instance() {
		return instance;
	}

	private MusicLibrary() {
	}

	public ObservableList<Song> initializeSongs(Path musicFolder) {
		songs.clear();
		albums.clear();
		artists.clear();
		items.clear();

		try {
			Files.walk(musicFolder, 5)
					.filter(path -> path.toString().toLowerCase().endsWith(".mp3")
							|| path.toString().toLowerCase().endsWith(".m4a"))
					.map(path -> Song.from(path.toUri())).filter(Objects::nonNull).forEach(song -> {
						songs.add(song);
						if (song.album != null && !albums.contains(song.album)) {
							albums.add(song.album);
						}
						if (song.artist != null && !artists.contains(song.artist)) {
							artists.add(song.artist);
						}
						items.add(song);
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return items;
	}

	public void filterOnCategory(Category category) {
		items.clear();
		switch (category) {
		case Songs:
			items.addAll(songs);
			break;
		case Playlists:
			// items.addAll(playlists);
			break;
		case Albums:
			break;
		case Artists:
			break;
		}
	}

	public void addPlaylist(List<Song> songs) {
		Playlist playlist = new Playlist(songs);
		playlists.add(playlist);

		items.clear();
		items.addAll(playlist.songs);
	}
}
