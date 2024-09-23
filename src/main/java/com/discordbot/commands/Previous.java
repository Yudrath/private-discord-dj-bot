package com.discordbot.commands;

import com.discordbot.*;
import com.discordbot.helpers.AudioChannelChecksHelper;
import com.discordbot.helpers.TextReplies;
import com.discordbot.helpers.EmbedReplies;
import com.discordbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import static com.discordbot.helpers.EventReplyHelper.*;

import java.util.List;

import static com.discordbot.helpers.EmbedReplies.negativeReplyEmbed;
import static com.discordbot.helpers.TextReplies.*;

public class Previous implements ICommand {
    @Override
    public String getName() {
        return "previous";
    }

    @Override
    public String getDescription() {
        return "Plays the previous track";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        AudioChannelChecksHelper helper = new AudioChannelChecksHelper(event);
        PlayerManager playerManager = PlayerManager.get();
        Guild eventGuild = event.getGuild();
        boolean previousTrackExists = playerManager.previousTrackExists(eventGuild);

        if (!helper.isBotInChannel()) {
            helper.botOpenAudioConnection();
        } else if (!helper.inSameChannelWithBot()) {
            replyTextWithDelete(MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT, true, event);

            return;
        }

        if (!playerManager.isPlaying(eventGuild) && !previousTrackExists) {
            replyTextWithDelete(NOTHING_HAS_BEEN_PLAYED_YET, true, event);

            return;
        }

        if (!playerManager.previousTrackExists(eventGuild)) {
            replyTextWithDelete(NO_PREVIOUS_TRACK, true, event);

            return;
        }

        event.deferReply().queue();

        playerManager.playPrevious(eventGuild, event);
    }
}
