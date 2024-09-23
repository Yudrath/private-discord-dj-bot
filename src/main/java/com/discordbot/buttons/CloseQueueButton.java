package com.discordbot.buttons;

import com.discordbot.AbstractButton;
import com.discordbot.helpers.MyEmojis;
import com.discordbot.helpers.EmbedReplies;
import com.discordbot.lavaplayer.QueueList;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import static com.discordbot.helpers.EventReplyHelper.replyEmbedWithDelete;

public class CloseQueueButton extends AbstractButton {

    public CloseQueueButton() {
        super(Button.danger("closequeue", MyEmojis.CLOSE));
    }

    @Override
    public void action(ButtonInteractionEvent event) {
        QueueList queueList = playerManager.getGuildMusicManager(event.getGuild()).getQueueList();
        queueList.getQueueListMessageReference().getMessage().delete().queue();

        replyEmbedWithDelete(EmbedReplies.positiveReplyEmbed("Queue screen has been closed."), true, event);
    }
}
