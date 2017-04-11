package utils;

import javafx.scene.media.MediaPlayer;
import models.Song;

/**
 * Created by bryancapps on 4/11/17.
 */
public interface MusicPlayerListener {
	void newSong(Song song);

	void playingStatusChanged(MediaPlayer.Status status);
}
