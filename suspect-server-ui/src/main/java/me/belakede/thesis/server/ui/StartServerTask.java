package me.belakede.thesis.server.ui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class StartServerTask extends Task<Void> {

    private final TextArea textArea;

    public StartServerTask(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    protected Void call() throws Exception {
        if (ServerProcess.getInstance().getProcess() == null) {
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "./suspect-server.jar");
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
