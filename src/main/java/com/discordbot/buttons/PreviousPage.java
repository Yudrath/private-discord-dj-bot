package com.discordbot.buttons;

import com.discordbot.AbstractButton;
import com.discordbot.helpers.MyEmojis;
import com.discordbot.lavaplayer.QueueList;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class PreviousPage extends AbstractButton {
    public PreviousPage() {
        super(Button.primary("previouspage", MyEmojis.LEFT_ARROW).asDisabled());
    }

    @Override
    public void action(ButtonInteractionEvent event) {
        QueueList queueList = playerManager.getGuildMusicManager(event.getGuild()).getQueueList();
        queueList.previousPage();
        playerManager.scrollQueue(event.getGuild(), event);
    }
}
