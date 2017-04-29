package utils;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import models.Playable;
import models.Song;
import models.Video;

public interface PlayerListener {
	void newSong(Song song);

	void newVideo(Video video);

	void playingStatusChanged(final CategoryType type, final MediaPlayer.Status status);
	
	void timeUpdated(final Playable media, final Duration elapsed, final Duration duration);

	void shuffleModeUpdated(final boolean shuffling);

	void loopModeUpdated(final boolean looping);

	void crossfadeModeUpdated(final boolean crossfade);
}
