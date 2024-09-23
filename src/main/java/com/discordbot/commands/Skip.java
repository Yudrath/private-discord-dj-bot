package com.discordbot.commands;

import com.discordbot.ICommand;
import com.discordbot.helpers.AudioChannelChecksHelper;
import static com.discordbot.helpers.EventReplyHelper.*;
import static com.discordbot.helpers.TextReplies.*;

import com.discordbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Skip implements ICommand {

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Skips the currently playing track";
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

        MessageEmbed embed = playerManager.skip(event.getGuild());

        replyEmbedWithDelete(embed, false, event);
    }
}
