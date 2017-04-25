package images;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.scene.image.Image;
import models.Video;
import utils.Player;

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
}
