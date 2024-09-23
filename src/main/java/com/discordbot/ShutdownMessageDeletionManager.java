package com.discordbot;

import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShutdownMessageDeletionManager {

    private static final List<MessageReference> messagesForDeletion = new ArrayList<>();

    private ShutdownMessageDeletionManager() {}

    public static void addMessage(MessageReference messageReference) {
        messagesForDeletion.add(messageReference);
    }

    public static void deleteMessages() {
        for (MessageReference entry : messagesForDeletion) {

            if (entry == null) {
                continue;
            }

            entry.deleteMessage();
        }
    }

    public static boolean isMessagesForDeletionEmpty() {
        return messagesForDeletion.isEmpty();
    }
}