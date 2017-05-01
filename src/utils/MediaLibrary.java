package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import models.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//import javafx.scene.image.Image;

public class MediaLibrary {
	private static MediaLibrary instance = new MediaLibrary();

	private ObservableList<Song> songs = FXCollections.observableArrayList();
	private ObservableList<Playlist> playlists = FXCollections.observableArrayList();
	private ObservableList<Album> albums = FXCollections.observableArrayList();
	private ObservableList<Artist> artists = FXCollections.observableArrayList();
	private ObservableList<Video> videos = FXCollections.observableArrayList();
	private ObservableList<Image> images = FXCollections.observableArrayList();

	public static MediaLibrary instance() {
		return instance;
	}

	private MediaLibrary() {
		if (saveLocation().exists()) {
			readSerializedLibrary(saveLocation());
		}
	}

	private File saveLocation() {
		File userDir = new File(System.getProperty("user.home"));
		File storageDir = new File(userDir, ".boomertuner");
		if (!storageDir.exists()) {
			storageDir.mkdir();
		}
		return new File(storageDir, "library.bin");
	}

	public void clearLibrary() {
		Player.instance().stop();
		songs.clear();
		playlists.clear();
		albums.clear();
		artists.clear();
		videos.clear();
		images.clear();
		if (!saveLocation().delete()) {
			System.out.println("Unable to delete library.bin");
		}
	}

	@SuppressWarnings("unchecked")
	private void readSerializedLibrary(final File location) {
		try {
			FileInputStream fileIn = new FileInputStream(location);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			songs = FXCollections.observableList((List<Song>) in.readObject());
			playlists = FXCollections.observableList((List<Playlist>) in.readObject());
			videos = FXCollections.observableList((List<Video>) in.readObject());
			images = FXCollections.observableList((List<Image>) in.readObject());
			for (Song song : songs) {
				if (!artists.contains(song.getArtist())) {
					artists.add(song.getArtist());
				}
				if (!albums.contains(song.getAlbum())) {
					albums.add(song.getAlbum());
				}
			}

			Collections.sort(albums, Comparator.comparing(Album::getName));
			Collections.sort(artists, Comparator.comparing(Artist::getName));
			Collections.sort(songs, Comparator.comparing(Song::getTitle));
			Collections.sort(images, Comparator.comparing(Image::getName));

			in.close();
			fileIn.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void writeSerializedLibrary() {
		try {
			FileOutputStream fileOut = new FileOutputStream(saveLocation());
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(new ArrayList<>(songs));
			out.writeObject(new ArrayList<>(playlists));
			out.writeObject(new ArrayList<>(videos));
			out.writeObject(new ArrayList<>(images));

			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in " + saveLocation());
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public void importPath(Path folder) {
		Task<Void> importTask = new Task<Void>() {
			@Override
			protected Void call() throws InterruptedException, IOException {
			List<Path> paths = Files.walk(folder, 5).collect(Collectors.toList());
			final int size = paths.size();
			for (int i = 0; i < size; i++) {
				if (Thread.currentThread().isInterrupted()) break;
				Path path = paths.get(i);
				if (Song.accepts(path)) {
					addSong(Song.from(path.toUri()));
				} else if (Video.accepts(path)) {
					addVideo(Video.from(path.toUri()));
				} else if(Image.accepts(path)){
					addImage(Image.from(path.toUri()));
				}
				updateProgress(i, size);
			}
			Collections.sort(albums, Comparator.comparing(Album::getName));
			Collections.sort(artists, Comparator.comparing(Artist::getName));
			Collections.sort(songs, Comparator.comparing(Song::getTitle));
			Collections.sort(images, Comparator.comparing(Image::getName));
			return null;
			}
		};
		Runnable serialize = this::writeSerializedLibrary;
		TaskRunner.run(importTask, "Importing Media...", serialize);
	}

	private boolean addSong(final Song song) {
		if (song == null || songs.contains(song)) { return false; }
		if (!artists.contains(song.getArtist())) { artists.add(song.getArtist()); }
		if (!albums.contains(song.getAlbum())) { albums.add(song.getAlbum()); }
		return songs.add(song);
	}

	private boolean addVideo(final Video video) {
		if (video == null || videos.contains(video)) { return false; }
		return videos.add(video);
	}

	private boolean addImage(final Image image) {
		if (image == null || images.contains(image)) { return false; }
		return images.add(image);
	}

	public Playlist addPlaylist(final String name, final List<? extends Playable> items) {
		Playlist playlist = new Playlist(name, items);
		playlists.add(playlist);
		Collections.sort(playlists, Comparator.comparing(Playlist::getName)); // Calls ChangeListener
		writeSerializedLibrary();
		return playlist;
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
	
	public ObservableList<Image> getImages() {
		return images;
	}
}
