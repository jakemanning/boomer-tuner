import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import models.Song;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Controller {
    public HBox bottom;
    public ImageView artwork;
    public ImageView previous;
    public ImageView play;
    public ImageView next;
    @FXML
    private TableView<Song> table;
    @FXML
    private TableColumn<Song, Integer> trackCol;
    @FXML
    private TableColumn<Song, String> titleCol;
    @FXML
    private TableColumn<Song, String> artistCol;
    @FXML
    private TableColumn<Song, String> albumCol;

    private final static Path musicFolder = Paths.get(System.getProperty("user.home"), "Music", "iTunes",
            "iTunes Media", "Music");
    private MediaPlayer mediaPlayer;

    @FXML
    private void initialize() {
        trackCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().track));
        titleCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().title));
        artistCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().artist));
        albumCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().album));

        try {
            previous.setImage(new Image(new FileInputStream("res/playercontrols/rewind.png")));
            play.setImage(new Image(new FileInputStream("res/playercontrols/play.png")));
            next.setImage(new Image(new FileInputStream("res/playercontrols/fastforward.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        loadSongs(table);
    }

    private void loadSongs(TableView<Song> list) {
        ObservableList<Song> items = FXCollections.observableArrayList();

        table.setItems(items);

        try {
            Files.walk(musicFolder, 5)
                    .filter(path -> path.toString().toLowerCase().endsWith(".mp3")
                            || path.toString().toLowerCase().endsWith(".m4a"))
                    .map(path -> Song.from(path.toUri()))
                    .filter(Objects::nonNull)
                    .forEach(items::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        list.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            if (mediaPlayer != null)
                mediaPlayer.stop();
            Media media = new Media(newValue.uri.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();

            try {
                BufferedImage image = (BufferedImage) newValue.artwork.getImage();
                artwork.setImage(SwingFXUtils.toFXImage(image, null));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
