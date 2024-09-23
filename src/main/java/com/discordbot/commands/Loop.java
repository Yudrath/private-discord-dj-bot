package com.discordbot.commands;

import com.discordbot.helpers.AudioChannelChecksHelper;
import com.discordbot.ICommand;
import com.discordbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import static com.discordbot.helpers.EventReplyHelper.*;

import java.util.List;

import static com.discordbot.helpers.TextReplies.*;

public class Loop implements ICommand {

    @Override
    public String getName() {
        return "loop";
    }

    @Override
    public String getDescription() {
        return "Puts the current track on loop or cancels the loop";
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

        MessageEmbed embed = playerManager.setOnLoop(eventGuild);
        replyEmbedWithDelete(embed, false, event);
    }
}
