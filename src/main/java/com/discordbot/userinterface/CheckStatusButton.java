package com.discordbot.userinterface;

import com.discordbot.helperbot.HelperBot;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.discordbot.DiscordBot;
import javafx.util.Duration;

public class CheckStatusButton {
    private static final int HEIGHT = 50;
    private static final int WIDTH = 140;
    private static final int IMAGE_WIDTH = 15;
    private static final int IMAGE_HEIGHT = 15;

    private Button checkStatusButton;

    private Timeline buttonTimeout;
    private static final int TIMEOUT_DURATION = 15000;

    private StatusLight statusLight;
    private MessageEmbedLabel messageEmbedLabel;
    private PowerButton powerButton;

    public CheckStatusButton(StatusLight light, MessageEmbedLabel label, PowerButton button, boolean botIsOnline) {
        this.statusLight = light;
        this.messageEmbedLabel = label;
        this.powerButton = button;

        instantiate();

        //If the DJ isn't online, and we can host it, we don't need the check status button enabled, because,
        //when attempting to start the button, we will run a check if it's been started before us
        checkStatusButton.setDisable(!botIsOnline);

        configureButtonTimeout();
        addListener();
    }

    public Button getCheckStatusButton() {
        return checkStatusButton;
    }

    private void instantiate() {
        checkStatusButton = new Button("Check bot's status");

        checkStatusButton.setMaxWidth(WIDTH);
        checkStatusButton.setMaxHeight(HEIGHT);

        checkStatusButton.setId("recheck-button");

        ImageView view = new ImageView(new Image("/recheckbutton.png"));
        view.setFitHeight(IMAGE_HEIGHT);
        view.setFitWidth(IMAGE_WIDTH);
        checkStatusButton.setGraphic(view);

        checkStatusButton.setContentDisplay(ContentDisplay.RIGHT);
    }

    private void configureButtonTimeout() {
        buttonTimeout = new Timeline();
        buttonTimeout.setCycleCount(1);
        buttonTimeout.getKeyFrames().add(new KeyFrame(
                        Duration.millis(TIMEOUT_DURATION),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                checkStatusButton.setDisable(false);
                            }
                        }));
    }

    public void setDisabled(boolean isDisabled) {
        checkStatusButton.setDisable(isDisabled);
    }

    private void addListener() {
        checkStatusButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                checkDJOnlineStatus();
                timeoutButton();
            }
        });
    }

    private void timeoutButton() {
        checkStatusButton.setDisable(true);
        buttonTimeout.play();
    }

    private void checkDJOnlineStatus() {
        HelperBot.runOnlineStatusCheck();

        try {
            for (int tries = 0; tries < 10; tries++) {

                if (HelperBot.isCheckDone()) {
                    break;
                }

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (HelperBot.isDJOnline()) {
            messageEmbedLabel.setMessage(MessageEmbedLabel.LabelMessage.HOSTED);
            setComponentsStatus(true);
        } else {
            messageEmbedLabel.setMessage(MessageEmbedLabel.LabelMessage.OFFLINE);
            setComponentsStatus(false);
            checkStatusButton.setDisable(true);
        }
    }

    private void setComponentsStatus(boolean status) {
        powerButton.setButtonDisabled(status);
        DiscordBot.setBotStatus(status);
        messageEmbedLabel.changeColour(status);
        statusLight.switchOn(status);
    }
}
