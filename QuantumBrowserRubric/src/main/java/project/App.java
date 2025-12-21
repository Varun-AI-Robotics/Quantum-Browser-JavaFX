package project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class App extends Application {

    private static Scene scene;
    private static PrimaryController primaryController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("primary.fxml"));
        Parent root = loader.load();
        primaryController = loader.getController();

        scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(Objects.requireNonNull(
                App.class.getResource("styles.css")).toExternalForm()
        );

        stage.setTitle("Quantum Browser");
        stage.setScene(scene);
        stage.setMinWidth(1200);
        stage.setMinHeight(700);
        stage.setMaximized(true);

        stage.setOnCloseRequest(event -> {
            primaryController.shutdown();
            Platform.exit();
        });

        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        scene.setRoot(loader.load());
        primaryController = loader.getController();
    }

    public static void main(String[] args) {
        launch();
    }
}
