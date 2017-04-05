package menu;

import utils.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryancapps on 4/4/17.
 */
public class MenuModel {
    private boolean playlistMode = false;
    private Category selectedCategory;
    private List<PlaylistModeListener> playlistListeners = new ArrayList<>();
    private List<SelectedCategoryListener> categoryListeners = new ArrayList<>();

    public boolean isPlaylistMode() {
        return playlistMode;
    }

    public void setPlaylistMode(boolean playlistMode) {
        this.playlistMode = playlistMode;
        playlistModeChanged();
    }

    public void togglePlaylistMode() {
        setPlaylistMode(!isPlaylistMode());
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
        categoryChanged();
    }

    public void addPlaylistModeListener(PlaylistModeListener listener) {
        playlistListeners.add(listener);
    }

    public void addSelectedCategoryListener(SelectedCategoryListener listener) {
        categoryListeners.add(listener);
    }

    private void categoryChanged() {
        for (SelectedCategoryListener listener : categoryListeners) {
            listener.selectedCategoryChanged(selectedCategory);
        }
    }

    private void playlistModeChanged() {
        for (PlaylistModeListener listener : playlistListeners) {
            listener.playlistModeChanged(playlistMode);
        }
    }
}
