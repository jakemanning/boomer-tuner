package albums;

import javafx.beans.value.ChangeListener;
import models.Album;

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
}
