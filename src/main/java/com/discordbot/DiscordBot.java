package com.discordbot;

import com.discordbot.listeners.ButtonListener;
import com.discordbot.listeners.CommandListener;
import com.discordbot.commands.*;
import com.discordbot.commands.Play;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.time.Duration;

public class DiscordBot {
    private static final String BOT_TOKEN = "";
    private static JDA bot;
    private static boolean botStatus = false; //false - off, true - on

    public static void launchBot() {
        ButtonManager.createButtons();

        CommandListener commandListener = new CommandListener();
        commandListener.add(new Play());
        commandListener.add(new Stop());
        commandListener.add(new Skip());
        commandListener.add(new Restart());
        commandListener.add(new SkipTo());
        commandListener.add(new Previous());
        commandListener.add(new Loop());
        commandListener.add(new Leave());
        commandListener.add(new Join());
        commandListener.add(new Queue());
        commandListener.add(new Clear());
        commandListener.add(new Pause());
        commandListener.add(new Unpause());
        commandListener.add(new NowPlaying());

        try {
            bot = JDABuilder.createDefault(BOT_TOKEN)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .addEventListeners(commandListener)
                    .addEventListeners(ButtonListener.get())
                    .build();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void shutdown() throws InterruptedException {
        ShutdownMessageDeletionManager.deleteMessages();
        bot.shutdown();
        // Allow at most 10 seconds for remaining requests to finish
        if (!bot.awaitShutdown(Duration.ofSeconds(10))) {
            bot.shutdownNow(); // Cancel all remaining requests
//            bot.awaitShutdown(); // Wait until shutdown is complete (indefinitely)
        }
    }

    public static void setBotStatus(boolean status) {
        botStatus = status;
    }

    public static boolean getBotStatus() {
        return botStatus;
    }

    public static JDA getBot() {
        return bot;
    }
}
