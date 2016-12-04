package me.belakede.thesis.server.ui;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.MasterDetailPane;
import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class ServerUiController implements Initializable {

    @FXML
    private Label generalSettingsLabel;
    @FXML
    private Label serverPortLabel;
    @FXML
    private Label debugModeLabel;
    @FXML
    private Label heartbeatSettingsLabel;
    @FXML
    private Label delayLabel;
    @FXML
    private Label periodLabel;
    @FXML
    private VBox imageView;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button exitButton;
    @FXML
    private MasterDetailPane masterDetailPane;
    @FXML
    private Button logsButton;
    @FXML
    private ChoiceBox<Language> language;
    @FXML
    private TextArea logArea;
    @FXML
    private Slider period;
    @FXML
    private Slider delay;
    @FXML
    private ToggleSwitch loggingLevel;
    @FXML
    private TextField serverPort;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changeLanguage(resources);
        hookupChangeListeners();
        setupLanguageBox();
    }

    private void setupLanguageBox() {
        language.getItems().addAll(Language.values());
        language.setConverter(new LanguageStringConverter());
        language.getSelectionModel().select(Language.HUNGARIAN);
        language.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            changeLanguage(ResourceBundle.getBundle("bundles/server-ui", newValue.getLocale()));
        });
    }

    private void changeLanguage(ResourceBundle bundle) {
        generalSettingsLabel.setText(bundle.getString("General settings"));
        serverPortLabel.setText(bundle.getString("Server port"));
        debugModeLabel.setText(bundle.getString("Debug mode"));
        heartbeatSettingsLabel.setText(bundle.getString("Heartbeat settings"));
        delayLabel.setText(bundle.getString("Delay"));
        periodLabel.setText(bundle.getString("Period"));
        startButton.setText(bundle.getString("Start"));
        stopButton.setText(bundle.getString("Stop"));
        exitButton.setText(bundle.getString("Exit"));
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
        StartServerTask task = new StartServerTask(logArea, serverPort.getText(), loggingLevel.isSelected(), delay.getValue(), period.getValue());
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
