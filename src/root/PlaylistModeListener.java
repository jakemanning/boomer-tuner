package root;

@FunctionalInterface
public interface PlaylistModeListener {
    void playlistModeChanged(boolean newValue);
}
