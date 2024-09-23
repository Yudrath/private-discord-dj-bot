package com.discordbot.userinterface;

import com.discordbot.DiscordBot;
import com.discordbot.helperbot.HelperBot;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class PowerButton {
    private static final int SQUARE_SIZE = 100;
    private static final int IMAGE_HEIGHT = SQUARE_SIZE / 2;
    private static final int IMAGE_WIDTH = SQUARE_SIZE / 2;

    private static final Image launchBotImage = new Image("/powerbutton.png");
    private static final Image terminateAppImage = new Image("/cross_symbol.png");

    private ImageView imageView;

    private StatusLight statusLight;
    private MessageEmbedLabel messageEmbedLabel;
    private CheckStatusButton checkStatusButton;

    private Timeline blockButtonTimeline;
    private static final int BLOCK_DURATION = 15000;

    private Button powerButton;

    private boolean isPressed;

    public PowerButton(StatusLight light, MessageEmbedLabel label, boolean botIsOnline) {
        this.statusLight = light;
        this.messageEmbedLabel = label;
        this.isPressed = false;

        instantiate(botIsOnline);
        configureTimeline();
        addListener();
    }

    public Button getPowerButton() {
        return powerButton;
    }

    private void instantiate(boolean botIsOnline) {
        this.powerButton = new Button();

        this.powerButton.setId("power-button");
        this.powerButton.setMinSize(SQUARE_SIZE, SQUARE_SIZE);

        imageView = new ImageView();
        imageView.setFitHeight(IMAGE_HEIGHT);
        imageView.setFitWidth(IMAGE_WIDTH);
        powerButton.setGraphic(imageView);

        setImage(launchBotImage);

        setButtonDisabled(botIsOnline);
    }

    private void setImage(Image image) {
        imageView.setImage(image);
    }

    private void configureTimeline() {
        blockButtonTimeline = new Timeline();
        blockButtonTimeline.setCycleCount(1);
        blockButtonTimeline.getKeyFrames().add(
                        new KeyFrame(Duration.millis(BLOCK_DURATION),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                powerButton.setDisable(false);
                            }
                        }));
    }

    private void addListener() {
        powerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (DiscordBot.getBotStatus()) {
                    try {
                        shutdown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (isDJAlreadyOnline()) return;
                    blockButton();
                    launch();
                }
            }
        });
    }

    public void setButtonDisabled(boolean isDisabled) {
        powerButton.setDisable(isDisabled);
    }

    public void setButtonPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public void setCheckStatusButton(CheckStatusButton checkStatusButton) {
        this.checkStatusButton = checkStatusButton;
    }

    public void setButtonPushed(boolean isPushed) {
        if (isPushed) {
            powerButton.setId("terminate-button");
            setImage(terminateAppImage);
        } else {
            powerButton.setId("power-button");
            setImage(launchBotImage);
        }
    }

    private void launch() {
        DiscordBot.launchBot();
        HelperBot.shutdown();
        messageEmbedLabel.setMessage(MessageEmbedLabel.LabelMessage.YOU_HOST);
        changeComponentStatus(true);
    }

    private void shutdown() throws InterruptedException {
        UserInterface.shutdownBots();
        System.exit(0);
    }

    private void changeComponentStatus(boolean status) {
        setButtonPushed(status);
        DiscordBot.setBotStatus(status);
        messageEmbedLabel.changeColour(status);
        checkStatusButton.setDisabled(status);
        statusLight.switchOn(status);
    }

    private void blockButton() {
        powerButton.setDisable(true);
        blockButtonTimeline.play();
    }

    private boolean isDJAlreadyOnline() {
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
            setButtonDisabled(true);
            setButtonPressed(false);
            DiscordBot.setBotStatus(true);
            messageEmbedLabel.changeColour(true);
            messageEmbedLabel.setMessage(MessageEmbedLabel.LabelMessage.HOSTED);
            checkStatusButton.setDisabled(false);
            statusLight.switchOn(true);

            return true;
        }

        return false;
    }
}
