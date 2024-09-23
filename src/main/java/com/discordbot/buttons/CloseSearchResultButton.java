package com.discordbot.buttons;

import com.discordbot.AbstractButton;
import com.discordbot.helpers.AudioChannelChecksHelper;
import com.discordbot.helpers.MyEmojis;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import static com.discordbot.helpers.EmbedReplies.positiveReplyEmbed;
import static com.discordbot.helpers.EventReplyHelper.replyEmbedWithDelete;
import static com.discordbot.helpers.EventReplyHelper.replyTextWithDelete;
import static com.discordbot.helpers.TextReplies.MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT;

public class CloseSearchResultButton extends AbstractButton {
    public CloseSearchResultButton() {
        super(Button.danger("closesearch", MyEmojis.CLOSE));
    }

    @Override
    public void action(ButtonInteractionEvent event) {
        AudioChannelChecksHelper helper = new AudioChannelChecksHelper(event);

        if (!helper.inSameChannelWithBot()) {
            replyTextWithDelete(MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT, true, event);

            return;
        }

        event.getMessage().delete().queue();
        replyEmbedWithDelete(positiveReplyEmbed("Search result has been closed."), true, event);
    }
}
