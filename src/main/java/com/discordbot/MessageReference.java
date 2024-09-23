package com.discordbot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;

import java.util.List;

//This class is needed to store the last playing embed in a channel
//We also need it to delete all the embeds of the playing tracks, when shutting down the bot
public class MessageReference {

    private Guild guild;
    private MessageChannel messageChannel;
    private Message message;

    private boolean messageIsDeleted;

    public MessageReference() {
        this.guild = null;
        this.messageChannel = null;
        this.message = null;
    }

    public MessageReference(Message message) {
        update(message);
    }

    public void update(Message message) {
        guild = message.getGuild();
        messageChannel = message.getChannel();
        this.message = message;
        messageIsDeleted = false;
    }

    public Guild getGuild() {
        return guild;
    }

    public long getGuildId() {
        return guild.getIdLong();
    }

    public MessageChannel getMessageChannel() {
        return messageChannel;
    }

    public long getMessageChannelId() {
        return messageChannel.getIdLong();
    }

    public Message getMessage() {
        return message;
    }

    public long getMessageId() {
        return message.getIdLong();
    }

    public void deleteMessage() {
        if (messageIsDeleted) {
            System.out.println("Message is already deleted.");
            return;
        }

        message.delete().queue();
        messageIsDeleted = true;
    }

    public boolean isMessageIsDeleted() {
        return messageIsDeleted;
    }

    public void editMessageComponents(List<LayoutComponent> layoutComponents) {
        message.editMessageComponents(layoutComponents).queue();
    }
}
