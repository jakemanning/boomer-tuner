package root;

import utils.CategoryType;

import java.util.ArrayList;
import java.util.List;

public class RootModel {
    private boolean playlistMode = false;
    private boolean shuffleMode = false;
    private boolean loopMode = false;
    private CategoryType selectedCategory;
    private List<SelectedCategoryListener> categoryListeners = new ArrayList<>();

    public void togglePlaylistMode() {
        playlistMode = !playlistMode;
        playlistModeChanged();
    }
    public void toggleShuffleMode() {
       shuffleMode = !shuffleMode;
       shuffleModeChanged();
    }

    public void toggleLoopMode() {
        loopMode = !loopMode;
        loopModeChanged();
    }

    public CategoryType getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(CategoryType selectedCategory) {
        this.selectedCategory = selectedCategory;
        categoryChanged();
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
    private void shuffleModeChanged() {
        for (ShuffleModeListener listener : shuffleListeners) {
            listener.shuffleModeChanged(shuffleMode);
        }
    }
    private void loopModeChanged() {
        for (LoopModeListener listener : loopListeners) {
            listener.loopModeChanged(loopMode);
        }
    }
}
