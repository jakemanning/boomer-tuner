package utils;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import models.Playable;
import models.Song;
import models.Video;

public interface PlayerListener {
	void newSong(Song song);

	void newVideo(Video video);

	void playingStatusChanged(CategoryType type, MediaPlayer.Status status);
	
	void timeUpdated(Playable media, Duration elapsed, Duration duration);
}
