package models;

import javafx.embed.swing.SwingFXUtils;
import org.jaudiotagger.tag.images.Artwork;
import utils.Icon;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Album implements Category, Serializable {
	private final String name;
	private transient Artwork artwork;
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
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Album album = (Album) o;

		if (!Objects.equals(this.name, album.getName()))
			return false;
		if (!Objects.equals(artist.getName(), album.artist.getName()))
			return false;
		return this.year == album.getYear();
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + artist.getName().hashCode();
		result = 31 * result + songs.hashCode();
		result = 31 * result + year;
		return result;
	}

	@Override
	public String toString() {
		return name;
	}

	public javafx.scene.image.Image getArtwork() {
		if (artwork == null) {
			artwork = songs.get(0).getArtwork();
		}
		try {
			return SwingFXUtils.toFXImage((BufferedImage) artwork.getImage(), null);
		} catch (NullPointerException | IOException | IllegalArgumentException e) {
			System.out.println("Failed to set artwork from music file");
			return Icon.DEFAULT_ARTWORK.image();
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
