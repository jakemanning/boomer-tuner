package artists;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import models.Artist;

public class ArtistsModel {
	private ObjectProperty<Artist> selectedArtist = new SimpleObjectProperty<>();
	private boolean directorySelected = false;

	public void setDirectorySelected(boolean selected){
		directorySelected = selected;
	}

	public boolean isDirectorySelected(){
		return directorySelected;
	}

	public Artist getSelectedArtist() {
		return selectedArtist.get();
	}

	public ObjectProperty<Artist> selectedArtistProperty() {
		return selectedArtist;
	}

	public void setSelectedArtist(final Artist selectedArtist) {
		this.selectedArtist.set(selectedArtist);
	}
}
