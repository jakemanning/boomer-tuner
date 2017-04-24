package artists;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import models.Artist;
import models.Song;
import root.RootModel;
import songs.SongsController;
import songs.SongsView;
import utils.CategoryView;
import utils.DirectoryListener;
import utils.MediaLibrary;

public class ArtistsView extends SplitPane implements CategoryView, DirectoryListener {
	private final ArtistsModel model;
	private final ArtistsController controller;
	private ListView<Artist> artists;
	private SongsView detail;

	public ArtistsView(ArtistsModel model, ArtistsController controller, RootModel rootModel) {
		this.model = model;
		this.controller = controller;
		initializeViews();

		initializeArtists();
		initializeDetailView();
		rootModel.addDirectoryListener(this::directorySet);
	}

	private void initializeArtists() {
		artists.setItems(MediaLibrary.instance().getArtists());
		artists.getSelectionModel().selectedItemProperty().addListener(controller.artistSelectionListener());
	}

	private void initializeDetailView() {
		if (model.isDirectorySelected() && MediaLibrary.instance().getAlbums().size() > 0) {
			detail.setPlaceholder(new Label("Select an artist from the list"));
		} else {
			detail.setPlaceholder(new Label("Import media to view artists"));
		}
		detail.getColumns().remove(2); // remove artist column
		model.selectedArtistProperty().addListener((observable, oldValue, newValue) -> {
			ObservableList<Song> items = MediaLibrary.instance().getSongs()
					.filtered(song -> song.getArtist().equals(newValue));
			detail.setItems(items);
		});
		if (model.getSelectedArtist() == null) {
			detail.setItems(null);
		}
	}

	private void initializeViews() {
		setPrefHeight(575);
		setPrefWidth(668);
		artists = new ListView<>();
		detail = new SongsView(new SongsController());
		getItems().addAll(artists, detail);
		setDividerPositions(0.28f);
	}

	@Override
	public void setRootModel(RootModel rootModel) {
		rootModel.setPlaylistModeListener(this::playlistModeChanged);
	}

	private void playlistModeChanged(boolean playlistMode) {
		detail.playlistModeChanged(playlistMode);
	}

	@Override
	public void directorySet(boolean set) {
		if(set) {
			detail.setPlaceholder(new Label("Select an artist from the list"));
		}
	}
}
