package com.discordbot.helpers;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class EventReplyHelper {
    private static final long DELETE_AFTER_TIME = 4000;

    private EventReplyHelper() {}

    public static void replyEmbed(MessageEmbed embed, boolean isEphemeral, GenericInteractionCreateEvent event) {
        if (event instanceof ButtonInteractionEvent) {
            ButtonInteractionEvent buttonEvent = (ButtonInteractionEvent) event;
            buttonEvent.replyEmbeds(embed).setEphemeral(isEphemeral).queue();
        } else if (event instanceof SlashCommandInteractionEvent) {
            SlashCommandInteractionEvent slashEvent = (SlashCommandInteractionEvent) event;
            slashEvent.replyEmbeds(embed).setEphemeral(isEphemeral).queue();
        }
    }

    public static void replyText(String text, boolean isEphemeral, GenericInteractionCreateEvent event) {
        if (event instanceof ButtonInteractionEvent) {
            ButtonInteractionEvent buttonEvent = (ButtonInteractionEvent) event;
            buttonEvent.reply(text).setEphemeral(isEphemeral).queue();
        } else if (event instanceof SlashCommandInteractionEvent) {
            SlashCommandInteractionEvent slashEvent = (SlashCommandInteractionEvent) event;
            slashEvent.reply(text).setEphemeral(isEphemeral).queue();
        }
    }

    public static void replyEmbedWithDelete(MessageEmbed embed, boolean isEphemeral, GenericInteractionCreateEvent event) {
        if (event instanceof ButtonInteractionEvent) {
            ButtonInteractionEvent buttonEvent = (ButtonInteractionEvent) event;
            buttonEvent.replyEmbeds(embed).setEphemeral(isEphemeral).queue((message) -> message.deleteOriginal().queueAfter(DELETE_AFTER_TIME, TimeUnit.MILLISECONDS));
        } else if (event instanceof SlashCommandInteractionEvent) {
            SlashCommandInteractionEvent slashEvent = (SlashCommandInteractionEvent) event;
            slashEvent.replyEmbeds(embed).setEphemeral(isEphemeral).queue((message) -> message.deleteOriginal().queueAfter(DELETE_AFTER_TIME, TimeUnit.MILLISECONDS));
        }
    }

    public static void replyTextWithDelete(String text, boolean isEphemeral, GenericInteractionCreateEvent event) {
        if (event instanceof ButtonInteractionEvent) {
            ButtonInteractionEvent buttonEvent = (ButtonInteractionEvent) event;
            buttonEvent.reply(text).setEphemeral(isEphemeral).queue((message) -> message.deleteOriginal().queueAfter(DELETE_AFTER_TIME, TimeUnit.MILLISECONDS));
        } else if (event instanceof SlashCommandInteractionEvent) {
            SlashCommandInteractionEvent slashEvent = (SlashCommandInteractionEvent) event;
            slashEvent.reply(text).setEphemeral(isEphemeral).queue((message) -> message.deleteOriginal().queueAfter(DELETE_AFTER_TIME, TimeUnit.MILLISECONDS));
        }
    }

    public static void replyHookEmbed(MessageEmbed embed, GenericInteractionCreateEvent event) {
        if (event instanceof ButtonInteractionEvent) {
            ButtonInteractionEvent buttonEvent = (ButtonInteractionEvent) event;
            buttonEvent.getHook().sendMessageEmbeds(embed).queue();
        } else if (event instanceof SlashCommandInteractionEvent) {
            SlashCommandInteractionEvent slashEvent = (SlashCommandInteractionEvent) event;
            slashEvent.getHook().sendMessageEmbeds(embed).queue();
        }
    }

    public static void replyHookEmbed(MessageEmbed embed, GenericInteractionCreateEvent event, List<LayoutComponent> layoutComponents) {
        if (event instanceof ButtonInteractionEvent) {
            ButtonInteractionEvent buttonEvent = (ButtonInteractionEvent) event;
            buttonEvent.getHook().sendMessageEmbeds(embed).setComponents(layoutComponents).queue();
        } else if (event instanceof SlashCommandInteractionEvent) {
            SlashCommandInteractionEvent slashEvent = (SlashCommandInteractionEvent) event;
            slashEvent.getHook().sendMessageEmbeds(embed).setComponents(layoutComponents).queue();
        }
    }

    public static void replyHookText(String text, GenericInteractionCreateEvent event) {
        if (event instanceof ButtonInteractionEvent) {
            ButtonInteractionEvent buttonEvent = (ButtonInteractionEvent) event;
            buttonEvent.getHook().sendMessage(text).queue();
        } else if (event instanceof SlashCommandInteractionEvent) {
            SlashCommandInteractionEvent slashEvent = (SlashCommandInteractionEvent) event;
            slashEvent.getHook().sendMessage(text).queue();
        }
    }

    public static void replyHookEmbedWithDelete(MessageEmbed embed, GenericInteractionCreateEvent event) {
        if (event instanceof ButtonInteractionEvent) {
            ButtonInteractionEvent buttonEvent = (ButtonInteractionEvent) event;
            buttonEvent.getHook().sendMessageEmbeds(embed).queue((message) -> message.delete().queueAfter(DELETE_AFTER_TIME, TimeUnit.MILLISECONDS));
        } else if (event instanceof SlashCommandInteractionEvent) {
            SlashCommandInteractionEvent slashEvent = (SlashCommandInteractionEvent) event;
            slashEvent.getHook().sendMessageEmbeds(embed).queue((message) -> message.delete().queueAfter(DELETE_AFTER_TIME, TimeUnit.MILLISECONDS));
        }
    }

    public static void replyHookEmbedWithDelete(MessageEmbed embed, GenericInteractionCreateEvent event, List<LayoutComponent> layoutcomponentList) {
        if (event instanceof ButtonInteractionEvent) {
            ButtonInteractionEvent buttonEvent = (ButtonInteractionEvent) event;
            buttonEvent
                    .getHook()
                    .sendMessageEmbeds(embed)
                    .setComponents(layoutcomponentList)
                    .queue((message) -> message.delete().queueAfter(DELETE_AFTER_TIME, TimeUnit.MILLISECONDS));
        } else if (event instanceof SlashCommandInteractionEvent) {
            SlashCommandInteractionEvent slashEvent = (SlashCommandInteractionEvent) event;
            slashEvent
                    .getHook()
                    .sendMessageEmbeds(embed)
                    .setComponents(layoutcomponentList)
                    .queue((message) -> message.delete().queueAfter(DELETE_AFTER_TIME, TimeUnit.MILLISECONDS));
        }
    }

    public static void replyHookTextWithDelete(String text, GenericInteractionCreateEvent event) {
        if (event instanceof ButtonInteractionEvent) {
            ButtonInteractionEvent buttonEvent = (ButtonInteractionEvent) event;
            buttonEvent.getHook().sendMessage(text).queue((message) -> message.delete().queueAfter(DELETE_AFTER_TIME, TimeUnit.MILLISECONDS));
        } else if (event instanceof SlashCommandInteractionEvent) {
            SlashCommandInteractionEvent slashEvent = (SlashCommandInteractionEvent) event;
            slashEvent.getHook().sendMessage(text).queue((message) -> message.delete().queueAfter(DELETE_AFTER_TIME, TimeUnit.MILLISECONDS));
        }
    }
}
