package com.discordbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageReference;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class GuildMusicManager {
    private final TrackScheduler scheduler;
    private final AudioPlayerSendHandler sendHandler;

    private String lastSearchQuery;
    private List<AudioTrack> searchQueryResult = new ArrayList<>();

    private QueueList queueList;

    public GuildMusicManager(AudioPlayerManager manager) {
        AudioPlayer player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
        sendHandler = new AudioPlayerSendHandler(player);
    }

    public void displayQueue(MessageChannel messageChannel) {
        if (queueList == null) {
            queueList = new QueueList(scheduler.getQueue(), messageChannel);
        }

        queueList.displayQueue();
    }

    public QueueList getQueueList() {
        return queueList;
    }

    public TrackScheduler getScheduler() {
        return scheduler;
    }

    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }

    public String getLastSearchQuery() {
        return lastSearchQuery;
    }

    public List<AudioTrack> getSearchQueryResult() {
        return searchQueryResult;
    }

    public void setLastSearchQuery(String query) {
        lastSearchQuery = query;
        System.out.println(lastSearchQuery);
    }
}
