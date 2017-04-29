package root;

@FunctionalInterface
public interface PlaylistModeListener {
    void playlistModeChanged(final boolean newValue);
}
