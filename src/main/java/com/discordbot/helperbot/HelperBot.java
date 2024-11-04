package com.discordbot.helperbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class HelperBot {
    private static final String BOT_TOKEN = "";
    private static final long GUILD_ID = 123456789L;
    private static final long DJ_ID = 123456789L;

    private static JDA bot;

    private static boolean djIsOnline = false;
    private static boolean checkIsDone;

    public static void run() {
        try {
            bot = JDABuilder.createDefault(BOT_TOKEN)
                    .enableIntents(GatewayIntent.GUILD_PRESENCES)
                    .enableCache(CacheFlag.ONLINE_STATUS)
                    .build()
                    .awaitReady();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runOnlineStatusCheck() {
        checkIsDone = false;

        Guild guild = bot.getGuildById(GUILD_ID);
        guild.retrieveMemberById(DJ_ID).queue(member -> {
            OnlineStatus onlineStatus = member.getOnlineStatus();

            if(onlineStatus.getKey().equals(OnlineStatus.ONLINE.getKey())) {
                djIsOnline = true;
            } else {
                djIsOnline = false;
            }

            checkIsDone = true;
        });
    }

    public static boolean isDJOnline() {
        return djIsOnline;
    }

    public static void setDJOnlineStatus(boolean isOnline) {
        djIsOnline = isOnline;
    }

    public static boolean isCheckDone() {
        return checkIsDone;
    }

    public static void shutdown() {
        bot.shutdown();
    }

    public static JDA getBot() {
        return bot;
    }
}
