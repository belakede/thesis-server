package me.belakede.thesis.server.ui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class StartServerTask extends Task<Void> {

    private final TextArea textArea;
    private final String port;
    private final String debug;
    private final String delay;
    private final String period;

    public StartServerTask(TextArea textArea, String port, boolean debug, double delay, double period) {
        this.textArea = textArea;
        this.port = (port.isEmpty()) ? "" : ("--server.port=" + port);
        this.debug = debug ? "--logging.level.me.belakede.thesis=DEBUG" : "";
        this.delay = "--heartbeat.delay=" + (delay * 1000);
        this.period = "--heartbeat.period=" + (period * 1000);
    }

    @Override
    protected Void call() throws Exception {
        if (ServerProcess.getInstance().getProcess() == null) {
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "./suspect-server.jar", port, debug, delay, period);
            Process process = processBuilder.start();
            ServerProcess.getInstance().setProcess(process);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String outputMessage = "";
            textArea.setText("");
            while ((outputMessage = in.readLine()) != null) {
                String finalOutputMessage = outputMessage;
                Platform.runLater(() -> {
                    textArea.appendText(finalOutputMessage);
                    textArea.appendText("\n");
                });
            }
            int status = process.waitFor();
        }
        return null;
    }
}
