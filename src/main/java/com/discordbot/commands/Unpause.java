package com.discordbot.commands;

import com.discordbot.ICommand;
import com.discordbot.helpers.AudioChannelChecksHelper;
import com.discordbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

import static com.discordbot.helpers.EventReplyHelper.*;
import static com.discordbot.helpers.TextReplies.*;

public class Unpause implements ICommand {

    @Override
    public String getName() {
        return "unpause";
    }

    @Override
    public String getDescription() {
        return "Unpauses the player";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild eventGuild = event.getGuild();
        PlayerManager manager = PlayerManager.get();
        AudioChannelChecksHelper helper = new AudioChannelChecksHelper(event);
        boolean trackIsPlaying = manager.isPlaying(eventGuild);

        if (!trackIsPlaying) {
            replyTextWithDelete(NOTHING_IS_PLAYING, true, event);

            return;
        }

        if (!manager.isPlayerPaused(eventGuild)) {
            replyTextWithDelete(PLAYER_IS_UNPAUSED_ALREADY, true, event);

            return;
        }

        if (!helper.inSameChannelWithBot()) {
            replyTextWithDelete(MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT, true, event);

            return;
        }

        MessageEmbed embed = manager.setPausedPlayer(eventGuild, false);
        replyEmbedWithDelete(embed, false, event);
    }
}
