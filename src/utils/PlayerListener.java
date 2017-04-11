package utils;

import javafx.scene.media.MediaPlayer;
import models.Song;
import models.Video;

public interface PlayerListener {
	void newSong(Song song);

	void newVideo(Video video);

	void playingStatusChanged(CategoryType type, MediaPlayer.Status status);
}
