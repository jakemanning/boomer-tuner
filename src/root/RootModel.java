package root;

import models.Playlist;
import utils.CategoryType;
import utils.DirectoryListener;

import java.util.ArrayList;
import java.util.List;

public class RootModel {
    private boolean playlistMode = false;
    private boolean directorySelected = false;
    private CategoryType selectedCategory;
	private PlaylistModeListener playlistListener = null;
	private List<DirectoryListener> directoryListeners = new ArrayList<>();
	private List<SelectedCategoryListener> categoryListeners = new ArrayList<>();
	private SearchListener searchListener = null;
	private String searchText = "";

	public boolean isPlaylistMode() {
		return playlistMode;
	}

    public void setPlaylistMode(final boolean playlistMode) {
        this.playlistMode = playlistMode;
        playlistModeChanged();
    }
    public void setDirectorySelection(final boolean selection){
        directorySelected = selection;
        directorySelectionChanged();
    }
    public void addDirectoryListener(final DirectoryListener listener){
        directoryListeners.add(listener);
        directorySelectionChanged();
    }
    public boolean isDirectorySelected(){
        return directorySelected;
    }

    public void togglePlaylistMode() {
        setPlaylistMode(!isPlaylistMode());
    }

    public CategoryType getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(final CategoryType selectedCategory) {
        this.selectedCategory = selectedCategory;
        categoryChanged();
    }

	public void setPlaylistModeListener(final PlaylistModeListener listener) {
		playlistListener = listener;
		playlistListener.playlistModeChanged(playlistMode);
	}

	public void playlistCreated(final Playlist playlist) {
        this.selectedCategory = CategoryType.Playlists;
        for (SelectedCategoryListener listener: categoryListeners) {
            listener.playlistCreated(playlist);
        }
    }

    public void addSelectedCategoryListener(final SelectedCategoryListener listener) {
        categoryListeners.add(listener);
    }

    private void categoryChanged() {
        for (SelectedCategoryListener listener : categoryListeners) {
            listener.selectedCategoryChanged(selectedCategory);
        }
    }

    private void playlistModeChanged() {
		if (playlistListener != null) {
			playlistListener.playlistModeChanged(playlistMode);
		}
	}
	private  void directorySelectionChanged() {
        for(DirectoryListener listener : directoryListeners){
            listener.directorySet(directorySelected);
        }
    }

	public void setSearchListener(final SearchListener listener) {
		searchListener = listener;
		searchListener.searchTextUpdated(searchText);
	}

	public void setSearchText(final String searchText) {
		this.searchText = searchText;
		if (searchListener != null) {
			searchListener.searchTextUpdated(searchText);
		}
	}
}
