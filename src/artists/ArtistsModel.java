package artists;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import models.Artist;

public class ArtistsModel {
	private ObjectProperty<Artist> selectedArtist = new SimpleObjectProperty<>();

	public Artist getSelectedArtist() {
		return selectedArtist.get();
	}

	public ObjectProperty<Artist> selectedArtistProperty() {
		return selectedArtist;
	}

	public void setSelectedArtist(Artist selectedArtist) {
		this.selectedArtist.set(selectedArtist);
	}
}
