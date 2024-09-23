package com.discordbot.listeners;

import com.discordbot.ICommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandListener extends ListenerAdapter {

    private List<ICommand> commands  = new ArrayList<>();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            for (ICommand command : commands) {
                guild.upsertCommand(command.getName(), command.getDescription()).addOptions(command.getOptions()).queue();
            }

//            guild.updateCommands().queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (ICommand command : commands) {
            if (command.getName().equals(event.getName())) {
                command.execute(event);

                return;
            }
        }
    }

    public void add(ICommand command) {
        commands.add(command);
    }

    public void deleteCommands(JDA jda) {
        List<Guild> guilds = jda.getGuilds();

        for (Guild guild : guilds) {
            deleteCommandsInGuild(guild);
        }
    }

    private void deleteCommandsInGuild(Guild guild) {
        RestAction<List<Command>> commandsList = guild.retrieveCommands();
        commandsList.queue(list -> {
            for (Command command : list) {
                System.out.println(command.getName());
                command.delete().queue();
            }
        });
    }
}
