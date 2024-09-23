package com.discordbot.commands;

import com.discordbot.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import static com.discordbot.helpers.EventReplyHelper.replyEmbed;

import java.util.List;

import static com.discordbot.helpers.EmbedReplies.nowPlayingEmbed;
import static com.discordbot.helpers.EventReplyHelper.replyEmbedWithDelete;

public class NowPlaying implements ICommand {
    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getDescription() {
        return "Outputs the currently playing track";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        replyEmbedWithDelete(nowPlayingEmbed(event.getGuild()), true, event);
    }
}
