package artists;

import javafx.beans.value.ChangeListener;
import models.Artist;

public class ArtistsController {
	private final ArtistsModel model;

	public ArtistsController(ArtistsModel model) {
		this.model = model;
	}

	ChangeListener<? super Artist> artistSelectionListener() {
		return (ChangeListener<Artist>) (observable, oldValue, newValue) -> {
			model.setSelectedArtist(newValue);
		};
	}
}
