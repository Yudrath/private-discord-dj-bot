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
import static com.discordbot.helpers.TextReplies.*;

import java.util.List;

public class Restart implements ICommand {
    @Override
    public String getName() {
        return "restart";
    }

    @Override
    public String getDescription() {
        return "Restarts playing the track from the start";
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

        if (!playerManager.isPlaying(eventGuild)) {
            replyTextWithDelete(NOTHING_IS_PLAYING, true, event);

            return;
        }

        if (!helper.inSameChannelWithBot()) {
            replyTextWithDelete(MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT, true, event);

            return;
        }

        event.deferReply().queue();

        replyHookEmbedWithDelete(EmbedReplies.trackRestartedEmbed(), event);

        playerManager.restartTrack(eventGuild);
    }
}
