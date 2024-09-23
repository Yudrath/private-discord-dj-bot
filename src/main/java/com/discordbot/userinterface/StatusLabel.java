package com.discordbot.userinterface;

import javafx.scene.control.Label;

public class StatusLabel {
    private static final String LABEL_CONTENT = "Status: ";
    private final Label statusLabel;

    public StatusLabel() {
        statusLabel = new Label(LABEL_CONTENT);
        statusLabel.setId("status-label");
    }

    public Label getStatusLabel() {
        return statusLabel;
    }
}
