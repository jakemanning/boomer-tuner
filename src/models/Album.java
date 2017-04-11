package models;

import javafx.embed.swing.SwingFXUtils;
import org.jaudiotagger.tag.images.Artwork;
import utils.Category;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Album implements Category {
	private final String name;
	private final Artwork artwork;
	private Artist artist;
	private List<Song> songs = new ArrayList<>();
	private int year;

	public Album(final String name, final Artwork artwork) {
		this.name = name;
		this.artwork = artwork;
	}

	public void addSong(final Song song) {
		artist = song.getArtist();
		songs.add(song);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Album album = (Album) o;

		if (!Objects.equals(this.name, album.getName()))
			return false;
		if (!Objects.equals(artist.getName(), album.artist.getName()))
			return false;
		if (this.year != album.getYear())
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + artist.hashCode();
		result = 31 * result + songs.hashCode();
		result = 31 * result + year;
		return result;
	}

	@Override
	public String toString() {
		return name;
	}

	public javafx.scene.image.Image getArtwork() {
		try {
			return SwingFXUtils.toFXImage((BufferedImage) artwork.getImage(), null);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getName() {
		return name;
	}

	public Artist getArtist() {
		return artist;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public int getYear() {
		return year;
	}
}
