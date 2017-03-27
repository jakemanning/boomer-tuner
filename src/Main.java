import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import models.Song;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Main extends Application {
    private final static Path musicFolder = Paths.get(System.getProperty("user.home"),
            "Music", "iTunes", "iTunes Media", "Music", "The Beatles");
    private MediaPlayer currentPlayer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 575));
        primaryStage.show();

        loadSongs((ListView<Song>) root.lookup("#center"));
    }

    private void loadSongs(ListView<Song> list) {
        ObservableList<Song> items = FXCollections.observableArrayList();

        list.setItems(items);

        try {
            Files.walk(musicFolder, 5)
                    .filter(path -> path.toString().toLowerCase().endsWith(".mp3")
                            || path.toString().toLowerCase().endsWith(".m4a"))
                    .map(path -> Song.from(path.toUri()))
                    .filter(Objects::nonNull)
                    .forEachOrdered(items::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        list.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            if (currentPlayer != null) currentPlayer.stop();
            Media media = new Media(newValue.getUri().toString());
            currentPlayer = new MediaPlayer(media);
            currentPlayer.play();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
