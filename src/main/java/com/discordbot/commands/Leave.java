package com.discordbot.commands;

import com.discordbot.helpers.AudioChannelChecksHelper;
import com.discordbot.ICommand;
import static com.discordbot.helpers.EmbedReplies.positiveReplyEmbed;
import com.discordbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import static com.discordbot.helpers.EventReplyHelper.*;

import java.util.List;

import static com.discordbot.helpers.TextReplies.*;

public class Leave implements ICommand {

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getDescription() {
        return "Disconnects the bot from the channel";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        AudioChannelChecksHelper helper = new AudioChannelChecksHelper(event);
        PlayerManager playerManager = PlayerManager.get();

        if (!helper.isBotInChannel()) {
            replyTextWithDelete(BOT_NOT_IN_AUDIO_CHANNEL, true, event);

            return;
        }

        if (!helper.inSameChannelWithBot()) {
            replyTextWithDelete(MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT, true, event);

            return;
        }

        String leaveMessage = String.format(BOT_LEAVING_AUDIO_CHANNEL, helper.getSelfChannelName());

        replyEmbedWithDelete(positiveReplyEmbed(leaveMessage), false, event);

        helper.botCloseAudioConnection();

        playerManager.pauseOnLeave(event.getGuild());
    }
}
