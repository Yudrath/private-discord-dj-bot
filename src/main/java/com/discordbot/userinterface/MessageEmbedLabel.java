package com.discordbot.userinterface;

import javafx.scene.control.Label;

public class MessageEmbedLabel {
    private Label messageEmbedLabel;
    private boolean isOnline;

    public enum LabelMessage {
        YOU_HOST("You are hosting the bot.\nIt's now ready to play music."),
        HOSTED("Someone else is hosting the bot.\nThe bot is ready to play music."),
        OFFLINE("The bot is currently offline,\nbut you can take the matter into\nyour own hands and host it.\nSimply click on the power\nbutton. =)"),
        SHUTTING_DOWN("The bot is currently in the process of shutting down\nYou will have to wait a bit before\nyou are able to start it again.");

        private String message;

        private LabelMessage(String status) {
            this.message = status;
        }

        public String getMessage() {
            return this.message;
        }
    }

    public MessageEmbedLabel(boolean isOnline) {
        this.isOnline = isOnline;
        instantiate();
    }

    private void instantiate() {
        messageEmbedLabel = new Label();
        messageEmbedLabel.setMinSize(200, 110);
        messageEmbedLabel.setPrefSize(200, 110);

        if (isOnline) {
            setMessage(LabelMessage.HOSTED);
        } else {
            setMessage(LabelMessage.OFFLINE);
        }

        changeColour(isOnline);
    }

    public Label getMessageEmbedLabel() {
        return messageEmbedLabel;
    }

    public void changeColour(boolean isOn) {
        if (isOn) {
            messageEmbedLabel.setId("label-positive");
        } else {
            messageEmbedLabel.setId("label-negative");
        }
    }

    public void setMessage(LabelMessage message) {
        messageEmbedLabel.setText(message.getMessage());
    }
}
