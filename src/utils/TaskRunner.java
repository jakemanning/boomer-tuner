package utils;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

class TaskRunner {
	static void run(Task task, String title) {
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);

		ProgressBar bar = new ProgressBar();
		bar.progressProperty().bind(task.progressProperty());

		Label label = new Label(title);

		final VBox box = new VBox();
		box.setPadding(new Insets(30));
		box.setSpacing(5);
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(label, bar);

		Scene scene = new Scene(box);
		stage.setScene(scene);

		task.setOnSucceeded(event -> stage.close());

		Thread thread = new Thread(task);
		thread.start();

		Task<Void> bufferTime = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		Thread bufferThread = new Thread(bufferTime);
		bufferThread.start();
		bufferTime.setOnSucceeded(event -> {
			if (task.isRunning()) {
				stage.show();
			}
		});
	}
}
