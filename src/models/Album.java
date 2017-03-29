package models;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;

public class Album {
	public final String name;
	public ImageView artwork;
	private List<Song> songs = new ArrayList<>();
	private int year;
	
	public Album(String name) {
		this.name = name;
	}
	
	public void addSong(Song song) {
		songs.add(song);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
