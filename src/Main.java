import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
		BorderPane root = loader.load();
		Controller controller = (Controller) loader.getController();
		controller.initializeMenuBar(primaryStage, root);

		Scene scene = new Scene(root);
		primaryStage.setTitle("Boomer Tuner");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
