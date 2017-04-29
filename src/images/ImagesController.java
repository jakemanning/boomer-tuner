package images;

import javafx.beans.value.ChangeListener;
import models.Image;

import java.util.List;
import java.util.function.Predicate;

public class ImagesController {

	public ChangeListener<Image> selectionListener(List<Image> images) {
		return (ov, oldValue, newValue) -> {
			if (newValue == null) {
				return; // If user selects new directory
			}
			//DISPLAY IMAAGE
			//Player.instance().playVideos(images, videos.indexOf(newValue));
		};
	}

	Predicate<Image> searchFilter(String searchText) {
		return image -> {
			String search = searchText.toLowerCase();
			return image.getName().toLowerCase().contains(search);
		};
	}
}
