package albums;

import javafx.beans.value.ChangeListener;
import models.Album;

import java.util.function.Predicate;

public class AlbumsController {
	private final AlbumsModel model;

	public AlbumsController(AlbumsModel model) {
		this.model = model;
	}

	ChangeListener<? super Album> albumSelectionListener() {
		return (ChangeListener<Album>) (observable, oldValue, newValue) -> {
			model.setSelectedAlbum(newValue);
		};
	}

	Predicate<Album> searchFilter(String searchText) {
		return album -> {
			String search = searchText.toLowerCase();
			String name = album.getName().toLowerCase();
			String artist = album.getArtist().getName().toLowerCase();
			String year = String.valueOf(album.getYear());
			return name.contains(search) || artist.contains(search) || year.contains(search);
		};
	}
}
