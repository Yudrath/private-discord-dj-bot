package com.discordbot.userinterface;

import com.discordbot.DiscordBot;
import com.discordbot.helperbot.HelperBot;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.dv8tion.jda.api.OnlineStatus;

import static com.discordbot.userinterface.SceneAssembler.*;

public class UserInterface extends Application {

    @Override
    public void start(Stage primaryStage) {

        HelperBot.run();
        HelperBot.runOnlineStatusCheck();

        waitUntilHelperBotIsReady();

        createComponents(HelperBot.isDJOnline());

        primaryStage.setResizable(false);
        primaryStage.setScene(buildScene());
        primaryStage.setTitle("Discord DJ Bot Launcher");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                try {
                    shutdownBots();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.exit(0);
            }
        });
        primaryStage.getIcons().add(new Image("/discord_bot_app_icon.png"));
        primaryStage.show();
    }

    private static void waitUntilHelperBotIsReady() {
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
    }

    public static void shutdownBots() throws InterruptedException {
        if (HelperBot.getBot() != null
                && HelperBot.getBot().getPresence().getStatus().getKey().equals(OnlineStatus.ONLINE.getKey())) {
            HelperBot.shutdown();
        }

        if (DiscordBot.getBot() != null
                && DiscordBot.getBotStatus()) {
            DiscordBot.shutdown();
        }
    }
}