package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Album;
import models.Artist;
import models.Playlist;
import models.Song;
import models.Video;

public class MediaLibrary {
	private static MediaLibrary instance = new MediaLibrary();

	private Map<Class<? extends Category>, Collection<? extends Category>> mediaLibrary;
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

	@SuppressWarnings("unchecked")
	public void initializeLibrary(Path folder) {
		try {
			final Map<Class<? extends Category>, Predicate<Path>> predicate = new TreeMap<>();
			predicate.put(Song.class, Song::accepts);
			predicate.put(Video.class, Video::accepts);

			Files.walk(folder, 5)
				.flatMap(path -> predicate.entrySet().stream().filter(entry -> entry.getValue().test(path))
						.map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), Category.from(path, entry.getKey()))));
							
							
//							.map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), Category.from(path, entry.getKey())))) // Creation of Category object
//					.filter(map -> Objects.nonNull(map.getValue())).collect(Collectors.groupingBy(Map.Entry::getKey, // Map from CategoryType to Category
//							Collectors.mapping(Map.Entry::getValue, Collectors.toCollection(FXCollections::observableArrayList)))); // Creates list out of collected items

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Category> ObservableList<T> get(Class<T> type) {
		return (ObservableList<T>) mediaLibrary.get(type);
	}

	public void addPlaylist(List<Song> songs) {
		Playlist playlist = new Playlist(songs);
		playlists.add(playlist);

		songs.clear();
		songs.addAll(playlist.getSongs());
	}

	public Map<CategoryType, ObservableList<Category>> getMediaLibrary() {
		return mediaLibrary;
	}
}
