package artists;

import javafx.beans.value.ChangeListener;
import models.Artist;

import java.util.function.Predicate;

public class ArtistsController {
	private final ArtistsModel model;

	public ArtistsController(final ArtistsModel model) {
		this.model = model;
	}

	ChangeListener<? super Artist> artistSelectionListener() {
		return (ChangeListener<Artist>) (observable, oldValue, newValue) -> {
			model.setSelectedArtist(newValue);
		};
	}

	Predicate<Artist> searchFilter(final String searchText) {
		return artist -> {
			String search = searchText.toLowerCase();
			String name = artist.getName().toLowerCase();
			return name.contains(search);
		};
	}
}
