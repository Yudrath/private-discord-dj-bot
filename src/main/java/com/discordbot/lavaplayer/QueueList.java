package com.discordbot.lavaplayer;

import com.discordbot.ButtonManager;
import com.discordbot.MessageReference;
import com.discordbot.helpers.EmbedReplies;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class QueueList {
    private List<String> trackList;
    private List<String> queuePages;

    private MessageReference queueListMessageReference;
    private MessageChannel queueListRequestChannel; //The channel where the call for displaying the queue was made

    private BlockingQueue<QueuedTrack> trackQueue;

    private int pageNumber;
    private int pageAmount;

    public QueueList(BlockingQueue<QueuedTrack> trackQueue, MessageChannel messageChannel) {
        this.queueListMessageReference = new MessageReference();
        this.trackQueue = trackQueue;
        this.queueListRequestChannel = messageChannel;
        this.trackList = new ArrayList<>();
        this.queuePages = new ArrayList<>();
        this.pageNumber = 0;
        this.pageAmount = 0;
    }

    public void displayQueue() {
        if (!trackList.isEmpty()) {
            trackList.clear();
            queuePages.clear();
        }

        this.pageNumber = 0;

        populateTrackList();
        createPages();
        sendPagesToChannel();
    }

    private void populateTrackList() {
        trackQueue.forEach(new Consumer<>() {

            int trackNumber = 1;
            StringBuilder stringBuilder = new StringBuilder();

            @Override
            public void accept(QueuedTrack queuedTrack) {
                String trackTitle = queuedTrack.getAudioTrack().getInfo().title;
                String trackUri = queuedTrack.getAudioTrack().getInfo().uri;
                String trackFormatted = String.format("[%s](%s)", trackTitle, trackUri);

                stringBuilder
                        .append(trackNumber)
                        .appendCodePoint(46)
                        .appendCodePoint(32)
                        .append(trackFormatted)
                        .appendCodePoint(10);

                trackList.add(stringBuilder.toString());

                stringBuilder.setLength(0);

                trackNumber++;
            }
        });
    }

    private void createPages() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < trackList.size(); i++) {
            stringBuilder.append(trackList.get(i));

            if (stringBuilder.length() >= 824) {
                queuePages.add(stringBuilder.toString());
                stringBuilder.setLength(0);
            }
        }

        queuePages.add(stringBuilder.toString());
        pageAmount = queuePages.size();
    }

    private void sendPagesToChannel() {
        if (queuePages.size() > 1) {
            queueListRequestChannel
                    .sendMessageEmbeds(EmbedReplies.queuePageEmbed(queuePages.get(pageNumber), pageNumber, pageAmount, trackList.size())) //Add a class field "currentPage"
                    .addActionRow(ButtonManager.getQueueButtons())
                    .queue(message -> queueListMessageReference.update(message));
        } else {
            queueListRequestChannel
                    .sendMessageEmbeds(EmbedReplies.queuePageEmbed(queuePages.get(pageNumber), pageNumber, pageAmount, trackList.size())) //Add a class field "currentPage"
                    .addActionRow(ButtonManager.getQueueButtonsNoArrows())
                    .queue(message -> queueListMessageReference.update(message));
        }
    }

    public MessageEmbed scrollPage() {
        return EmbedReplies.queuePageEmbed(queuePages.get(pageNumber), pageNumber, pageAmount, trackList.size());
    }

    public void nextPage() {
        pageNumber++;
    }

    public void previousPage() {
        pageNumber--;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageAmount() {
        return pageAmount;
    }

    public MessageReference getQueueListMessageReference() {
        return queueListMessageReference;
    }
}
