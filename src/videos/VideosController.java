package videos;

import javafx.beans.value.ChangeListener;
import models.Video;
import utils.Player;

import java.util.List;

public class VideosController {

	public ChangeListener<Video> selectionListener(List<Video> videos) {
		return (ov, oldValue, newValue) -> {
			if (newValue == null) {
				return; // If user selects new directory
			}
			Player.instance().playVideos(videos, videos.indexOf(newValue));
		};
	}
}