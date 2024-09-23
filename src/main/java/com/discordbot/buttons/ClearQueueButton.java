package com.discordbot.buttons;

import com.discordbot.AbstractButton;
import com.discordbot.helpers.MyEmojis;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import static com.discordbot.helpers.EventReplyHelper.replyEmbedWithDelete;

public class ClearQueueButton extends AbstractButton {
    public ClearQueueButton() {
        super(Button.success("clearqueue", MyEmojis.CLEAR));
    }

    @Override
    public void action(ButtonInteractionEvent event) {
        replyEmbedWithDelete(playerManager.emptyQueue(event.getGuild()), false, event);
    }
}
