package com.discordbot.commands;

import com.discordbot.helpers.AudioChannelChecksHelper;
import com.discordbot.ICommand;
import com.discordbot.helpers.TextReplies;
import com.discordbot.helpers.EmbedReplies;
import com.discordbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

import static com.discordbot.helpers.EventReplyHelper.*;
import static com.discordbot.helpers.TextReplies.*;

public class Stop implements ICommand {
    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "Stops the player";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        AudioChannelChecksHelper helper = new AudioChannelChecksHelper(event);
        PlayerManager playerManager = PlayerManager.get();
        Guild guild = event.getGuild();

        if (!playerManager.isPlaying(guild)) {
            replyTextWithDelete(NOTHING_IS_PLAYING, true, event);

            return;
        }

        if (!helper.inSameChannelWithBot()) {
            replyTextWithDelete(MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT, true, event);

            return;
        }

        MessageEmbed embed = playerManager.stopPlayer(guild);

        replyEmbedWithDelete(embed, false, event);

        helper.botCloseAudioConnection();
    }
}
