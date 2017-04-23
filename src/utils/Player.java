package utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import models.Category;
import models.Playable;
import models.Song;
import models.Video;

import java.util.*;

public class Player {

	private static Player instance = new Player();
	public static Player instance() {
		return instance;
	}

	private BooleanProperty shuffleMode = new SimpleBooleanProperty(false);
	private BooleanProperty loopMode = new SimpleBooleanProperty(false);
	private BooleanProperty crossfadeMode = new SimpleBooleanProperty(false);
	private Random random = new Random();

	private Player() {
	}

	private List<PlayerListener> listeners = new ArrayList<>();

	private MediaPlayer currentPlayer;
	private List<Playable> playQueue = new ArrayList<>();
	private Deque<Playable> lastPlayed = new ArrayDeque<>();
	private int currentIndex;

	public MediaView getView() {
		return new MediaView(currentPlayer);
	}

	private void setCurrentPlayer(MediaPlayer player) {
		currentPlayer = player;
		Category newItem = playQueue.get(currentIndex);
		if (CategoryType.of(newItem) == CategoryType.Songs) {
			newSongBeingPlayed();
		} else if (CategoryType.of(newItem) == CategoryType.Videos) {
			newVideoBeingPlayed();
		}
		currentPlayer.setOnEndOfMedia(() -> {
			lastPlayed.addFirst(playQueue.get(currentIndex));
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
	}

	private void play(List<? extends Playable> items, int index) {
		if (currentPlayer != null && currentPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
			currentPlayer.stop();
		}
		playQueue = new ArrayList<>(items);
		currentIndex = index;
		setCurrentPlayer(new MediaPlayer(new Media(playQueue.get(currentIndex).getUri().toString())));
		currentPlayer.play();
	}

	private void playCrossfade(List<? extends Playable> items, int index) {
		MediaPlayer oldPlayer = currentPlayer;
		final double currentVolume = oldPlayer.getVolume();
		playQueue = new ArrayList<>(items);
		currentIndex = index;
		MediaPlayer newPlayer = new MediaPlayer(new Media(playQueue.get(currentIndex).getUri().toString()));
		newPlayer.setVolume(0);
		setCurrentPlayer(newPlayer);
		newPlayer.play();
		Timeline crossfade = new Timeline(new KeyFrame(Duration.seconds(3),
				new KeyValue(oldPlayer.volumeProperty(), 0),
				new KeyValue(newPlayer.volumeProperty(), currentVolume)));
		crossfade.setOnFinished(event -> oldPlayer.stop());
		crossfade.play();
	}

	private InvalidationListener crossfadeTimeListener() {
		return new InvalidationListener() {
			private boolean run = false;

			@Override
			public void invalidated(javafx.beans.Observable observable) {
				// Run once when less than 3 seconds away from the end of the song
				if (currentPlayer.getCurrentTime()
						.greaterThan(currentPlayer.getMedia().getDuration().subtract(Duration.seconds(3)))
						&& !run && crossfadeMode.get()) {
					run = true;
					currentPlayer.setOnEndOfMedia(() -> {
					});
					lastPlayed.addFirst(playQueue.get(currentIndex));
					playCrossfade(playQueue, nextIndex());
				}
			}
		};
	}

	public void toggleShuffle() {
		shuffleMode.set(!shuffleMode.get());
	}
	public void toggleLoop() {
		loopMode.set(!loopMode.get());
	}

	public void toggleCrossfade() {
		crossfadeMode.set(!crossfadeMode.get());
	}

	public void playSongs(List<Song> songs, int index) {
		lastPlayed.clear();
		play(songs, index);
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
			return random.nextInt(playQueue.size());
		}
		return (currentIndex + 1) % playQueue.size();
	}

	public void playVideos(List<Video> videos, int index) {
		lastPlayed.clear();
		play(videos, index);
	}

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

	public void previous() {
		if (currentPlayer != null && currentPlayer.getCurrentTime().greaterThan(Duration.seconds(7))) {
			currentPlayer.seek(Duration.ZERO);
		} else {
			play(playQueue, previousIndex());
		}
	}

	public void seek(double value) {
		currentPlayer.seek(currentPlayer.getMedia().getDuration().multiply(value));
	}

	public void next() {
		if (currentPlayer != null) {
			lastPlayed.addFirst(playQueue.get(currentIndex));
			play(playQueue, nextIndex());
		}
	}

	public void addListener(PlayerListener listener) {
		listeners.add(listener);
		shuffleMode.addListener(obs -> listener.shuffleModeUpdated(shuffleMode.get()));
		loopMode.addListener(obs -> listener.loopModeUpdated(loopMode.get()));
		crossfadeMode.addListener(obs -> listener.crossfadeModeUpdated(crossfadeMode.get()));
	}

	public BooleanProperty shuffleModeProperty() {
		return shuffleMode;
	}

	public BooleanProperty loopModeProperty() {
		return loopMode;
	}

	public BooleanProperty crossfadeModeProperty() {
		return crossfadeMode;
	}

	private void playingStatusChanged(MediaPlayer.Status status) {
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
}
