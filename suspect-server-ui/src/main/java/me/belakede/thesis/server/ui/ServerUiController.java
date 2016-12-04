package me.belakede.thesis.server.ui;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.MasterDetailPane;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class ServerUiController implements Initializable {

    @FXML
    private VBox imageView;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private MasterDetailPane masterDetailPane;
    @FXML
    private Button logsButton;
    @FXML
    private ChoiceBox language;
    @FXML
    private TextArea logArea;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hookupChangeListeners();
        setupLanguageBox();
    }

    private void setupLanguageBox() {
        language.getItems().addAll(Language.values());
        language.setConverter(new LanguageStringConverter());
        language.getSelectionModel().select(Language.ENGLISH);
    }

    private void hookupChangeListeners() {
        ServerProcess serverProcess = ServerProcess.getInstance();
        serverProcess.processProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                FadeTransition transition = new FadeTransition(Duration.millis(3000), imageView);
                transition.setFromValue(1.0);
                transition.setToValue(0.1);
                transition.setCycleCount(20);
                transition.setAutoReverse(true);
                transition.setDuration(Duration.seconds(0.75));
                imageView.getStyleClass().add("green");
                transition.play();
            } else {
                imageView.getStyleClass().removeAll(Collections.singleton("green"));
            }
        });
    }

    public void toggleLogs() {
        if (masterDetailPane.isShowDetailNode()) {
            stage.setHeight(225);
            logsButton.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.ANGLE_DOUBLE_DOWN));
        } else {
            stage.setHeight(600);
            logsButton.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.ANGLE_DOUBLE_RIGHT));
        }
        masterDetailPane.setShowDetailNode(!masterDetailPane.isShowDetailNode());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void startServer() {
        StartServerTask task = new StartServerTask(logArea);
        task.setOnFailed(event -> stopServer());
        task.setOnSucceeded(event -> startButton.setDisable(false));
        task.setOnRunning(event -> startButton.setDisable(true));
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void stopServer() {
        StopServerTask task = new StopServerTask();
        task.setOnRunning(event -> stopButton.setDisable(true));
        task.setOnSucceeded(event -> startButton.setDisable(false));
        task.setOnFailed(event -> stopButton.setDisable(false));
        new Thread(task).start();
    }

    public void exitUi() {
        stopServer();
        Platform.exit();
    }
}
