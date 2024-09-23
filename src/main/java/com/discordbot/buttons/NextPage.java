package com.discordbot.buttons;

import com.discordbot.AbstractButton;
import com.discordbot.helpers.MyEmojis;
import com.discordbot.lavaplayer.QueueList;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class NextPage extends AbstractButton {
    public NextPage() {
        super(Button.primary("nextpage", MyEmojis.RIGHT_ARROW));
    }

    @Override
    public void action(ButtonInteractionEvent event) {
        QueueList queueList = playerManager.getGuildMusicManager(event.getGuild()).getQueueList();
        queueList.nextPage();
        playerManager.scrollQueue(event.getGuild(), event);
    }
}
