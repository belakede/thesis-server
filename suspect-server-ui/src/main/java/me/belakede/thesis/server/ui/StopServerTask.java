package me.belakede.thesis.server.ui;

import javafx.concurrent.Task;

public class StopServerTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        ServerProcess serverProcess = ServerProcess.getInstance();
        if (serverProcess != null && serverProcess.getProcess().isAlive()) {
            serverProcess.getProcess().destroy();
            serverProcess.setProcess(null);
        }
        return null;
    }
}
