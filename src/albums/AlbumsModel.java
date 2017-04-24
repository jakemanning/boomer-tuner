package albums;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import models.Album;
import models.Artist;

public class AlbumsModel {
	private ObjectProperty<Album> selectedAlbum = new SimpleObjectProperty<>();
	private boolean directorySelected = false;

	public void setDirectorySelected(boolean selected){
		directorySelected = selected;
	}
	public boolean isDirectorySelected(){
		return directorySelected;
	}
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
