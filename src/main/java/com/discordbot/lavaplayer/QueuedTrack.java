package com.discordbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class QueuedTrack {
    private AudioTrack audioTrack;
    private TextChannel textChannel;

    public QueuedTrack(AudioTrack audioTrack, TextChannel textChannel) {
        this.audioTrack = audioTrack;
        this.textChannel = textChannel;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public AudioTrack getAudioTrack() {
        return audioTrack;
    }

    public void setTextChannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    public void setAudioTrack(AudioTrack audioTrack) {
        this.audioTrack = audioTrack;
    }

    public void cloneTrack() {
        audioTrack = audioTrack.makeClone();
    }
}
