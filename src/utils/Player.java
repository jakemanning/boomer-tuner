package utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import models.Category;
import models.Playable;
import models.Song;
import models.Video;

import java.util.*;
import java.util.prefs.Preferences;

public class Player {

	private static Player instance = new Player();
	public static Player instance() {
		return instance;
	}
	private Player() { initPreferences(); }

	private final Preferences preferences = Preferences.userNodeForPackage(Player.class);
	private BooleanProperty shuffleMode = new SimpleBooleanProperty(false);
	private BooleanProperty loopMode = new SimpleBooleanProperty(false);
	private BooleanProperty crossfadeMode = new SimpleBooleanProperty(false);
	private DoubleProperty volume = new SimpleDoubleProperty(1.00);
	private static final int CROSSFADE_DURATION = 3;
	private Random random = new Random();
	private final int secondThreshold = 7;
	private List<PlayerListener> listeners = new ArrayList<>();
	private MediaPlayer currentPlayer;
	private List<Playable> playQueue = new ArrayList<>();
	private Deque<Playable> lastPlayed = new ArrayDeque<>();
	private int currentIndex;
	private final int maxDeckSize = 30;

	public MediaView getView() {
		return new MediaView(currentPlayer);
	}

	// region Media Player=
    private void initPreferences() {
        Preferences preferences = Preferences.userNodeForPackage(Player.class);
        shuffleMode.set(preferences.getBoolean("ShuffleMode", false));
        loopMode.set(preferences.getBoolean("LoopMode", false));
        crossfadeMode.set(preferences.getBoolean("CrossFadeMode", false));
        volume.setValue(preferences.getDouble("Volume", 1.00));
    }

	private void setCurrentPlayer(final MediaPlayer player) {
		currentPlayer = player;
		Category newItem = playQueue.get(currentIndex);
		switch (CategoryType.of(newItem)) {
			case Songs: newSongBeingPlayed(); break;
			case Videos: newVideoBeingPlayed(); break;
		}

		currentPlayer.setOnEndOfMedia(() -> {
			if (lastPlayed.size() > maxDeckSize) {
				lastPlayed.pollLast();
				lastPlayed.addFirst(playQueue.get(currentIndex));
			}

			currentIndex = nextIndex();
			Media media = new Media(playQueue.get(currentIndex).getUri().toString());
			setCurrentPlayer(new MediaPlayer(media));
			currentPlayer.play();
		});
		currentPlayer.statusProperty()
				.addListener((observable, oldValue, newValue) -> playingStatusChanged(newValue));
		currentPlayer.currentTimeProperty().addListener(ov -> timeUpdated());
		currentPlayer.currentTimeProperty().addListener(crossfadeTimeListener());
		currentPlayer.setOnReady(this::timeUpdated);
		currentPlayer.volumeProperty().bind(volume);
	}

	private void playCrossfade(final List<? extends Playable> items, final int index) {
		MediaPlayer oldPlayer = currentPlayer;
		final double currentVolume = oldPlayer.getVolume();
		oldPlayer.volumeProperty().unbind();
		playQueue = new ArrayList<>(items);
		currentIndex = index;

		MediaPlayer newPlayer = new MediaPlayer(new Media(playQueue.get(currentIndex).getUri().toString()));
		newPlayer.setVolume(0);
		newPlayer.play();
		Timeline crossfade = new Timeline(new KeyFrame(Duration.seconds(CROSSFADE_DURATION),
				new KeyValue(oldPlayer.volumeProperty(), 0),
				new KeyValue(newPlayer.volumeProperty(), currentVolume)));
		crossfade.setOnFinished(event -> {
			oldPlayer.stop();
			setCurrentPlayer(newPlayer);
		});
		crossfade.play();
	}

	private int previousIndex() {
		if (loopMode.get()) {
			return currentIndex;
		}
		if (!lastPlayed.isEmpty()) {
			return playQueue.indexOf(lastPlayed.removeFirst());
		}
		int newIndex = ((currentIndex - 1) % playQueue.size());
		if (newIndex < 0) {
			newIndex = newIndex + playQueue.size();
		}
		return newIndex;
	}

	private int nextIndex() {
		if (loopMode.get()) {
			return currentIndex;
		}
		if (shuffleMode.get()) {
			int nextIndex = (random.nextInt(playQueue.size()));
			while(playQueue.size() > 1 && nextIndex == currentIndex) {
				nextIndex = (random.nextInt(playQueue.size()));
			}
			return nextIndex;

		}
		return (currentIndex + 1) % playQueue.size();
	}
	// endregion

	// region Toggling
	public void toggleShuffle() {
		shuffleMode.set(!shuffleMode.get());
		preferences.putBoolean("ShuffleMode", shuffleMode.get());
	}
	public void toggleLoop() {
		loopMode.set(!loopMode.get());
		preferences.putBoolean("LoopMode", loopMode.get());
	}
	public void toggleCrossfade() {
		crossfadeMode.set(!crossfadeMode.get());
		preferences.putBoolean("CrossFadeMode", crossfadeMode.get());
	}
	// endregion

	// region Play
	public void playVideos(final List<Video> videos, final int index) {
		lastPlayed.clear();
		play(videos, index);
	}

	public void playSongs(final List<Song> songs, final int index) {
		lastPlayed.clear();
		play(songs, index);
	}

	private void play(final List<? extends Playable> items, final int index) {
		if (currentPlayer != null) {
			currentPlayer.stop();
		}
		playQueue = new ArrayList<>(items);
		currentIndex = index;
		setCurrentPlayer(new MediaPlayer(new Media(playQueue.get(currentIndex).getUri().toString())));
		currentPlayer.play();
	}
	// endregion

	// region Listeners
	public void addListener(final PlayerListener listener) {
		listeners.add(listener);
		shuffleMode.addListener(obs -> listener.shuffleModeUpdated(shuffleMode.get()));
		loopMode.addListener(obs -> listener.loopModeUpdated(loopMode.get()));
		crossfadeMode.addListener(obs -> listener.crossfadeModeUpdated(crossfadeMode.get()));
	}

	private InvalidationListener crossfadeTimeListener() {
		return new InvalidationListener() {
			private boolean run = false;

			@Override
			public void invalidated(final javafx.beans.Observable observable) {
				// Run once when less than 3 seconds away from the end of the song
				if (currentPlayer.getCurrentTime()
						.greaterThan(currentPlayer.getMedia().getDuration()
								.subtract(Duration.seconds(CROSSFADE_DURATION)))
						&& !run && crossfadeMode.get()) {
					run = true;
					currentPlayer.setOnEndOfMedia(() -> {
						// clear out the normal behaviour for the end of a song
						// playCrossfade will start the next song a little early
						// so there's no need to do anything once the current song ends
					});
					lastPlayed.addFirst(playQueue.get(currentIndex));
					playCrossfade(playQueue, nextIndex());
				}
			}
		};
	}

	private void playingStatusChanged(final MediaPlayer.Status status) {
		for (PlayerListener listener : listeners) {
			CategoryType type = CategoryType.of(playQueue.get(currentIndex));
			listener.playingStatusChanged(type, status);
		}
	}

	private void newSongBeingPlayed() {
		for (PlayerListener listener : listeners) {
			listener.newSong((Song) playQueue.get(currentIndex));
		}
	}

	private void newVideoBeingPlayed() {
		for (PlayerListener listener : listeners) {
			listener.newVideo((Video) playQueue.get(currentIndex));
		}
	}

	private void timeUpdated() {
		for (PlayerListener listener : listeners) {
			listener.timeUpdated(playQueue.get(currentIndex), currentPlayer.getCurrentTime(), currentPlayer.getTotalDuration());
		}
	}
	// endregion

	// region Properties
	public BooleanProperty shuffleModeProperty() {
		return shuffleMode;
	}

	public BooleanProperty loopModeProperty() {
		return loopMode;
	}

	public BooleanProperty crossfadeModeProperty() {
		return crossfadeMode;
	}

	public double getVolume() {
		return volume.get();
	}

	public DoubleProperty volumeProperty() {
		return volume;
	}

	public void setVolume(final double volume) {
		this.volume.set(volume);
		saveVolume();
	}

	public void saveVolume() {
		preferences.putDouble("Volume", volume.get());
	}
	// endregion

	// region Basic Functions
	public void resume() {
		if (currentPlayer != null) {
			currentPlayer.play();
		}
	}

	public boolean isPlaying() {
		return currentPlayer != null && currentPlayer.getStatus().equals(javafx.scene.media.MediaPlayer.Status.PLAYING);
	}

	public void pause() {
		if (currentPlayer != null) {
			currentPlayer.pause();
		}
	}

	public void stop() {
		if (currentPlayer != null) {
			currentPlayer.stop();
			currentPlayer = null;
		}
	}

	public void previous() {
		if (currentPlayer != null && currentPlayer.getCurrentTime().greaterThan(Duration.seconds(secondThreshold))) {
			currentPlayer.seek(Duration.ZERO);
		} else if (currentPlayer != null) {
			play(playQueue, previousIndex());
		}
	}

	public void seek(final double value) {
		if (currentPlayer != null) {
			currentPlayer.seek(currentPlayer.getMedia().getDuration().multiply(value));
		}
	}

	public void next() {
		if (currentPlayer != null) {
			lastPlayed.addFirst(playQueue.get(currentIndex));
			play(playQueue, nextIndex());
		}
	}
	// endregion
}
