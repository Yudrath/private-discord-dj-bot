package com.discordbot.commands;

import com.discordbot.ICommand;
import com.discordbot.helpers.AudioChannelChecksHelper;
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

import static com.discordbot.helpers.EventReplyHelper.replyEmbedWithDelete;
import static com.discordbot.helpers.EventReplyHelper.replyTextWithDelete;
import static com.discordbot.helpers.TextReplies.MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT;
import static com.discordbot.helpers.TextReplies.NOTHING_IS_PLAYING;

public class SkipTo implements ICommand {

    @Override
    public String getName() {
        return "skipto";
    }

    @Override
    public String getDescription() {
        return "Skips to the given track number (check /queue for track numerations)";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.INTEGER, "to", "track number", true)
                .setMinValue(1));

        return data;
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

        MessageEmbed embed = playerManager.skipToTrack(eventGuild, event.getOption("to").getAsInt());

        replyEmbedWithDelete(embed, false, event);
    }
}
