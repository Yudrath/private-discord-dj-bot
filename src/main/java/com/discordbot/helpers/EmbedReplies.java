package com.discordbot.helpers;

import com.discordbot.lavaplayer.GuildMusicManager;
import com.discordbot.lavaplayer.PlayerManager;
import com.discordbot.lavaplayer.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sun.nio.sctp.MessageInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import static com.discordbot.helpers.MyEmojis.*;

import java.util.List;

import java.awt.Color;

public class EmbedReplies {

    private EmbedReplies() {}

    public static MessageEmbed queuePageEmbed(String page, int pageNumber, int pageAmount, int trackAmount) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.PINK);
        eb.setTitle(PAGE.getFormatted() + " Queue");
        eb.setDescription("Tracks in queue: " + trackAmount);
        eb.addField(String.format("Page: %s/%s", (pageNumber + 1), pageAmount), page, false);
        return eb.build();
    }

    public static MessageEmbed trackRestartedEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);
        eb.setDescription(RESTART.getFormatted() + " Track has been restarted.");
        return eb.build();
    }

    public static MessageEmbed stopPlayerEmbed(TrackScheduler scheduler) {
        int queueSize = scheduler.getQueue().size();
        String description;

        if (queueSize > 0) {
            description = STOP.getFormatted() + " The player has been stopped and " + queueSize + " tracks have been removed from the queue.";
        } else {
            description = STOP.getFormatted() + " The player has been stopped.";
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.RED);
        eb.setDescription(description);

        return eb.build();
    }

    public static MessageEmbed setOnLoopEmbed(boolean onLoop) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.PINK);
        String message;

        if (onLoop) {
            message = LOOP_ON.getFormatted() + " Current track has been set on loop.";
        } else {
            message = LOOP_OFF.getFormatted() + " Current track is no more on loop.";
        }

        eb.setDescription(message);

        return eb.build();
    }

    public static MessageEmbed playlistLoadedEmbed(AudioPlaylist audioPlaylist, String playlistURL) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.PINK);

        String playlistHyperlink = String.format("[%s](%s).", audioPlaylist.getName(), playlistURL);
        String message = CHECK_MARK.getFormatted() + " Found and added " + audioPlaylist.getTracks().size() + " tracks to the queue from the playlist " + playlistHyperlink;

        embedBuilder.setDescription(message);

        return embedBuilder.build();
    }

    public static MessageEmbed searchResultEmbed(String platform, List<AudioTrack> foundTracks) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField(MAGNIFYING_GLASS.getFormatted() + " Search result for **" + platform + "**:", foundTracksToString(foundTracks), false);

        return eb.build();
    }

    private static String foundTracksToString(List<AudioTrack> foundTracks) {
        StringBuilder message = new StringBuilder();

        for (int i = 1; i <= foundTracks.size(); i++) {
            AudioTrack track = foundTracks.get(i - 1);

            message.append(i);
            message.append(". ");
            message.append(track.getInfo().title);
            message.appendCodePoint(10);
        }

        return message.toString();
    }

    public static MessageEmbed skippedSongEmbed(TrackScheduler scheduler) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);

        String currentTrackTitle = scheduler.getCurrentAudioTrack().getInfo().title;
        String currentTrackURI = scheduler.getCurrentAudioTrack().getInfo().uri;

        String embedMessage = String.format(SKIP.getFormatted() + " Track [%s](%s) has been skipped.", currentTrackTitle, currentTrackURI);

        eb.setDescription(embedMessage);

        return eb.build();
    }

    public static MessageEmbed queueEmptiedEmbed(TrackScheduler scheduler) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);
        String description;

        int queueSize = scheduler.getQueue().size();

        description = WASTEBASKET.getFormatted() + " Removed " + queueSize + " tracks from the queue.";

        eb.setDescription(description);

        return eb.build();
    }

    public static MessageEmbed nowPlayingEmbed(Guild guild) {
        TrackScheduler scheduler = PlayerManager.get().getGuildMusicManager(guild).getScheduler();

        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (!scheduler.isPlaying()) {
            embedBuilder.setColor(Color.RED);
            embedBuilder.setDescription(FRUSTRATED_FACE.getFormatted() + " Nothing is playing currently.");

            return embedBuilder.build();
        }

        embedBuilder.setColor(Color.PINK);

        AudioTrackInfo audioTrackInfo = scheduler.getCurrentAudioTrack().getInfo();

        String currentTrackTitle = audioTrackInfo.title;
        String currentTrackURI = audioTrackInfo.uri;
        long trackLength = audioTrackInfo.length;

        String embedMessage = String.format(NOTES.getFormatted() + " [%s](%s) - (%s)", currentTrackTitle, currentTrackURI, millisecondsToDuration(trackLength));

        embedBuilder.setTitle(PLAY.getFormatted() + " Now playing");
        embedBuilder.setDescription(embedMessage);

        return embedBuilder.build();
    }

    public static MessageEmbed pauseEmbed(TrackScheduler scheduler, boolean isPaused) {
        String playerStatus;

        if (isPaused) {
            playerStatus = " has been paused.";
        } else {
            playerStatus = " has been unpaused.";
        }

        EmbedBuilder eb = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setDescription(NOTES.getFormatted() + " Track: " + scheduler.getTrackTitle() + playerStatus);

        return eb.build();
    }

    public static MessageEmbed queuedTrackEmbed(AudioTrack audioTrack) {
        String trackTitle = audioTrack.getInfo().title;
        String url = audioTrack.getInfo().uri;

        String titleHyperlink = String.format("[%s](%s)", trackTitle, url);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.PINK);
        eb.setDescription(CHECK_MARK.getFormatted() + " Added " + titleHyperlink + " to the queue.");

        return eb.build();
    }

    public static MessageEmbed playingTrackEmbed(AudioTrack audioTrack) {
        String trackTitle = audioTrack.getInfo().title;
        String url = audioTrack.getInfo().uri;
        long trackLength = audioTrack.getInfo().length;

        String titleHyperlink = String.format("[%s](%s)", trackTitle, url);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.PINK);
        eb.setTitle(PLAY.getFormatted() + " Now playing");
        eb.addField(NOTES.getFormatted() + " Song", titleHyperlink, false);
        eb.addField(CLOCK.getFormatted() + " Duration", millisecondsToDuration(trackLength), false);

        return eb.build();
    }

    public static MessageEmbed negativeReplyEmbed(String message) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.RED);
        eb.setDescription(THUMBS_DOWN.getFormatted() + " " + message);

        return eb.build();
    }

    public static MessageEmbed positiveReplyEmbed(String message) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);
        eb.setDescription(THUMBS_UP.getFormatted() + " " + message);

        return eb.build();
    }

    private static String millisecondsToDuration(long milliseconds) {
        int seconds = (int)(milliseconds / 1000);
        int minutes = 0;
        int hours = 0;

        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }

        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }

        String secondsFormatted = formatTime(seconds);
        String minutesFormatted = formatTime(minutes);
        String hoursFormatted = formatTime(hours);

        return hoursFormatted + ":" + minutesFormatted + ":" + secondsFormatted;
    }

    private static String formatTime(int time) {
        String timeFormatted;

        if (time < 10) {
            timeFormatted = "0" + time;
        } else {
            timeFormatted = String.valueOf(time);
        }

        return timeFormatted;
    }
}
