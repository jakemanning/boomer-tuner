package albums;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import models.Album;

public class AlbumsModel {
	private ObjectProperty<Album> selectedAlbum = new SimpleObjectProperty<>();
	public Album getSelectedAlbum() {
		return selectedAlbum.get();
	}

	public ObjectProperty<Album> selectedAlbumProperty() {
		return selectedAlbum;
	}

	public void setSelectedAlbum(Album selectedAlbum) {
		this.selectedAlbum.set(selectedAlbum);
	}
}
