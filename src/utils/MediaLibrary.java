package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MediaLibrary {
	private static MediaLibrary instance = new MediaLibrary();

	private ObservableList<Song> songs = FXCollections.observableArrayList();
	private ObservableList<Playlist> playlists = FXCollections.observableArrayList();
	private ObservableList<Album> albums = FXCollections.observableArrayList();
	private ObservableList<Artist> artists = FXCollections.observableArrayList();
	private ObservableList<Video> videos = FXCollections.observableArrayList();

	public static MediaLibrary instance() {
		return instance;
	}

	private MediaLibrary() {
	}

	public void importPath(Path folder) {
		try {
			Files.walk(folder, 5)
					.forEach(path -> {
						if (Song.accepts(path)) {
							Song song = Song.from(path.toUri());
							if (song != null && !songs.contains(song)) {
								songs.add(song);
								artists.add(song.getArtist());
								albums.add(song.getAlbum());
							}
						} else if (Video.accepts(path)) {
							videos.add(Video.from(path.toUri()));
						}
					});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addPlaylist(List<Song> songs) {
		Playlist playlist = new Playlist(songs);
		playlists.add(playlist);
	}

	public ObservableList<Song> getSongs() {
		return songs;
	}

	public ObservableList<Playlist> getPlaylists() {
		return playlists;
	}

	public ObservableList<Album> getAlbums() {
		return albums;
	}

	public ObservableList<Artist> getArtists() {
		return artists;
	}

	public ObservableList<Video> getVideos() {
		return videos;
	}
}
