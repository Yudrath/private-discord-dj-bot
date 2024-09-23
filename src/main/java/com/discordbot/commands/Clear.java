package com.discordbot.commands;

import com.discordbot.ICommand;
import com.discordbot.helpers.AudioChannelChecksHelper;
import com.discordbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import static com.discordbot.helpers.EventReplyHelper.replyEmbedWithDelete;

import java.util.List;

public class Clear implements ICommand {
    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "Empties the queue";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        PlayerManager playerManager = PlayerManager.get();
        replyEmbedWithDelete(playerManager.emptyQueue(event.getGuild()), false, event);
    }
}
