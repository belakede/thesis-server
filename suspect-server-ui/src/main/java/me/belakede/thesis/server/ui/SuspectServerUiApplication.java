package me.belakede.thesis.server.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class SuspectServerUiApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles/server-ui", Language.HUNGARIAN.getLocale());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("server-ui.fxml"), resourceBundle);
        BorderPane root = (BorderPane) loader.load();
        ServerUiController controller = (ServerUiController) loader.getController();
        controller.setStage(stage);
        stage.setTitle("Suspect Server UI");
        stage.setScene(new Scene(root, 800, 225));
        stage.setHeight(225);
        stage.show();
    }
}
