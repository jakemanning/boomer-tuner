package videos;

import javafx.beans.value.ChangeListener;
import models.Video;
import utils.Player;

import java.util.List;
import java.util.function.Predicate;

public class VideosController {

	public ChangeListener<Video> selectionListener(List<Video> videos) {
		return (ov, oldValue, newValue) -> {
			if (newValue == null) {
				return; // If user selects new directory
			}
			Player.instance().playVideos(videos, videos.indexOf(newValue));
		};
	}

	public Predicate<Video> searchFilter(String searchText) {
		String search = searchText.toLowerCase();
		return video -> video.getTitle().toLowerCase().contains(search);
	}
}