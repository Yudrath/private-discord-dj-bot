package com.discordbot.lavaplayer;

import com.discordbot.ButtonManager;
import com.discordbot.MessageReference;
import com.discordbot.helpers.EmbedReplies;
import com.discordbot.ShutdownMessageDeletionManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.discordbot.helpers.EmbedReplies.*;

public class TrackScheduler extends AudioEventAdapter {

    private final BlockingQueue<QueuedTrack> queue = new LinkedBlockingQueue<>();
    private final AudioPlayer player;

    //Stores a reference of the embed which is used as an audio player for the player
    private MessageReference messageReference;
    private QueuedTrack currentTrack;

    private AudioTrack previousTrack;

    private boolean onLoop;
    private boolean nowPlayingPrevious;
    private boolean replayInterruptedByPrevious;
    private boolean playingRestartedTrack;

    public TrackScheduler(AudioPlayer player) {
        previousTrack = null;
        this.player = player;
        onLoop = false;
        nowPlayingPrevious = false;
        replayInterruptedByPrevious = false;
        playingRestartedTrack = false;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

        if (!playingRestartedTrack) {
            previousTrack = track.makeClone();
        }

        playingRestartedTrack = false;


        if (endReason.mayStartNext) {

            //A song can be looped only if it ends naturally (plays thoroughly),
            //if it ends abruptly, it means it was skipped
            //This is why it's in the body of this if statement
            if (onLoop) {
                this.player.playTrack(track.makeClone());

                return;
            }

            nextTrack();
        }
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        ButtonManager.getPausePlay().changeStatus(true);
        messageReference.editMessageComponents(ButtonManager.getPlayerControls());
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        ButtonManager.getPausePlay().changeStatus(false);
        messageReference.editMessageComponents(ButtonManager.getPlayerControls());
    }

    public void stopTrack() {
        deleteCurrentTrackMessage();
        player.stopTrack();
    }

    public void queue(QueuedTrack queuedTrack) {
        if (!player.startTrack(queuedTrack.getAudioTrack(), true)) {
            queue.offer(queuedTrack);
        } else {
            currentTrack = queuedTrack;
        }
    }

    public void playPreviousTrack() {
        ButtonManager.getLoopButton().changeStatus(false);

        TextChannel channelToSendEmbed = currentTrack.getTextChannel();

        if (nowPlayingPrevious) { //If we're already playing the previous track, do nothing
            return;
        }

        messageReference.deleteMessage();

        nowPlayingPrevious = true;

        channelToSendEmbed
                .sendMessageEmbeds(EmbedReplies.playingTrackEmbed(previousTrack))
                .setComponents(ButtonManager.getPlayerControls())
                .queue(message -> messageReference.update(message));

//        nowPlayingPrevious = false; //If the previous track ends, obviously, you aren't playing the previous anymore
        replayInterruptedByPrevious = true;

        player.startTrack(previousTrack, false);

        //Might be a better idea to place it somehow in the onTrackEnd event?
//        previousTrack = null; //Might be a problem? Will the player complain?
    }

    public void restartCurrentTrack() {
        playingRestartedTrack = true;
//        deleteCurrentTrackMessage();
        AudioTrack trackClone = currentTrack.getAudioTrack().makeClone();
        currentTrack.setAudioTrack(trackClone);
        player.startTrack(trackClone, false);
    }

    public boolean previousTrackIsSet() {
        return previousTrack != null;
    }

    public boolean playTrackInterruptedByPrevious() {
        if (currentTrack.getAudioTrack() == null) {
            replayInterruptedByPrevious = false;
            return false;
        }

        currentTrack.cloneTrack();

        MessageChannel messageChannel = currentTrack.getTextChannel();

        AudioTrack track = currentTrack.getAudioTrack();

        player.startTrack(track, false);

        messageChannel
                .sendMessageEmbeds(EmbedReplies.playingTrackEmbed(track))
                .setComponents(ButtonManager.getPlayerControls())
                .queue(message -> messageReference.update(message));

        replayInterruptedByPrevious = false;

        return true;
    }

    public void nextTrack() {
        ButtonManager.getLoopButton().changeStatus(false);

        messageReference.deleteMessage();


        if (nowPlayingPrevious) {
            player.stopTrack();
        }

        onLoop = false;
        nowPlayingPrevious = false;

        if (replayInterruptedByPrevious) {
            if (playTrackInterruptedByPrevious()) {
                return;
            }
        }

        if (queue.isEmpty()) {
            player.stopTrack();
            currentTrack.setAudioTrack(null); //If there's no other track to play, audioTrack is null
            return;
        }

        currentTrack = queue.poll();
        AudioTrack audioTrack = currentTrack.getAudioTrack();
        MessageEmbed embed = playingTrackEmbed(audioTrack);
        currentTrack
                .getTextChannel()
                .sendMessageEmbeds(embed)
                .setComponents(ButtonManager.getPlayerControls())
                .queue((message) -> {
                    messageReference.update(message);
                });

        player.startTrack(audioTrack, false);
    }

    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }

    public void skipTracks(int skips) {
        for (int i = skips; i > 1; i--) { //i > 1 because the last one we should play
            queue.poll();
        }

        nextTrack();
    }

    public boolean isPlaying() {
        return player.getPlayingTrack() != null;
    }

    public boolean isPlayerPaused() {
        return player.isPaused();
    }

    public void emptyQueue() {
        queue.clear();
    }

    public void setPausedPlayer(boolean isPaused) {
        player.setPaused(isPaused);
    }

    public void deleteCurrentTrackMessage() {
            messageReference.deleteMessage();
    }

    public void setMessageReference(Message message) {
        if (messageReference == null) {
            messageReference = new MessageReference(message);
            ShutdownMessageDeletionManager.addMessage(messageReference);
        } else {
            messageReference.update(message);
        }
    }

    public Message getCurrentTrackMessage() {
        return messageReference.getMessage();
    }

    public AudioTrack getPreviousTrack() {
        return previousTrack;
    }

    public void setOnLoop (boolean onLoop) {
        this.onLoop = onLoop;
        ButtonManager.getLoopButton().changeStatus(onLoop);
        messageReference.editMessageComponents(ButtonManager.getPlayerControls());
    }

    public boolean getOnLoop() {
        return onLoop;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public String getTrackTitle() {
        return player.getPlayingTrack().getInfo().title;
    }

    public AudioTrack getCurrentAudioTrack() {
        return player.getPlayingTrack();
    }

    public BlockingQueue<QueuedTrack> getQueue() {
        return queue;
    }

    public boolean isNowPlayingPrevious() {
        return nowPlayingPrevious;
    }
}
