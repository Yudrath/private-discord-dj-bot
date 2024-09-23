package com.discordbot.userinterface;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SceneAssembler {
    public static final double SCENE_WIDTH = 350;
    public static final double SCENE_HEIGHT = 230;

    private static MessageEmbedLabel messageEmbedLabel;
    private static StatusLight statusLight;
    private static StatusLabel statusLabel;
    private static PowerButton powerButton;
    private static CheckStatusButton checkStatusButton;

    private SceneAssembler() {}

    public static void createComponents(boolean botIsOnline) {
        messageEmbedLabel = new MessageEmbedLabel(botIsOnline);
        statusLight = new StatusLight(botIsOnline);
        statusLabel = new StatusLabel();
        powerButton = new PowerButton(statusLight, messageEmbedLabel, botIsOnline);
        checkStatusButton = new CheckStatusButton(statusLight, messageEmbedLabel, powerButton, botIsOnline);

        powerButton.setCheckStatusButton(checkStatusButton); //We want to disable the checkStatusButton when we host the button
    }

    public static Scene buildScene() {
        Scene scene = new Scene(assembleUI(), SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add(SceneAssembler.class.getResource("/styles.css").toExternalForm());

        return scene;
    }

    private static VBox assembleUI() {
        VBox root = new VBox();
        root.setSpacing(SCENE_HEIGHT / 10);

        root.getChildren().addAll(statusComponents(), EmbedAndPowerButton());

        return root;
    }

    private static VBox statusComponents() {
        HBox light = new HBox();
        light.setAlignment(Pos.TOP_CENTER);
        light.setSpacing(5);
        light.setPadding(new Insets((SCENE_HEIGHT / 10), 0, 0, 0));

        light
                .getChildren()
                .addAll(statusLabel.getStatusLabel(),
                        statusLight.getLight());

        VBox lightPlusButton = new VBox();
        lightPlusButton.setAlignment(Pos.TOP_CENTER);
        lightPlusButton.setSpacing(15);

        lightPlusButton
                .getChildren()
                .addAll(light,
                        checkStatusButton.getCheckStatusButton());

        return lightPlusButton;
    }

    private static HBox EmbedAndPowerButton() {
        HBox hbox = new HBox();
        hbox.setSpacing(15);
        hbox.setAlignment(Pos.CENTER);

        hbox
                .getChildren()
                .addAll(powerButton.getPowerButton(),
                        messageEmbedLabel.getMessageEmbedLabel());

        return hbox;
    }
}
