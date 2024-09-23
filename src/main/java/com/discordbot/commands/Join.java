package com.discordbot.commands;

import com.discordbot.ICommand;
import com.discordbot.helpers.AudioChannelChecksHelper;
import com.discordbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static com.discordbot.helpers.TextReplies.*;
import static com.discordbot.helpers.EmbedReplies.*;
import static com.discordbot.helpers.AudioChannelChecksHelper.*;
import static com.discordbot.helpers.EventReplyHelper.*;

import java.util.List;

public class Join implements ICommand {
    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getDescription() {
        return "Joins the bot to your audio channel";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        AudioChannelChecksHelper helper = new AudioChannelChecksHelper(event);
        PlayerManager playerManager = PlayerManager.get();

        if (!helper.isMemberInChannel()) {
            replyTextWithDelete(MEMBER_NOT_IN_AUDIO_CHANNEL, true, event);

            return;
        }

        if (helper.inSameChannelWithBot()) {
            replyTextWithDelete(BOT_AND_MEMBER_IN_SAME_AUDIO_CHANNEL, true, event);

            return;
        }

        if (playerManager.isPlaying(event.getGuild())) {
            playerManager.setPausedPlayer(event.getGuild(), false);
        }

        String channelName = helper.getMemberChannelName();

        replyEmbedWithDelete(positiveReplyEmbed("Joining audio channel **" + channelName + "**."), false, event);
        helper.botOpenAudioConnection();
    }
}
