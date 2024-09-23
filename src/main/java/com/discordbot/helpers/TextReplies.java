package com.discordbot.helpers;

public final class TextReplies {
    private TextReplies() {};

    public static final String NOTHING_IS_PLAYING = "The player is empty at the moment.";
    public static final String NOTHING_HAS_BEEN_PLAYED_YET = "The player hasn't yet played anything";
    public static final String NO_PREVIOUS_TRACK = "There's no previous track I can play.";

    public static final String MEMBER_NOT_IN_AUDIO_CHANNEL = "You must be in an audio channel to use this command.";
    public static final String MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT = "You need to be in the same voice channel as me.";
    public static final String BOT_AND_MEMBER_IN_SAME_AUDIO_CHANNEL = "We are in the same audio channel already.";
    public static final String BOT_NOT_IN_AUDIO_CHANNEL = "Currently not connected to any voice channel.";
    public static final String BOT_LEAVING_AUDIO_CHANNEL = "Leaving audio channel **%s**.";

    public static final String PLAYER_IS_PAUSED_ALREADY = "The player is paused already.";
    public static final String PLAYER_IS_UNPAUSED_ALREADY = "The player is unpaused already.";

}
