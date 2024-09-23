package com.discordbot.buttons;

import com.discordbot.AbstractButton;
import com.discordbot.helpers.AudioChannelChecksHelper;
import com.discordbot.helpers.MyEmojis;
import com.discordbot.helpers.TextReplies;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import static com.discordbot.helpers.EventReplyHelper.replyEmbedWithDelete;
import static com.discordbot.helpers.EventReplyHelper.replyTextWithDelete;

public class LoopButton extends AbstractButton {
    private static final UnicodeEmoji LOOP_ON = MyEmojis.LOOP_ON;
    private static final UnicodeEmoji LOOP_OFF = MyEmojis.LOOP_OFF;

    public LoopButton() {
        super(Button.primary("loop", LOOP_OFF));
    }

    @Override
    public void action(ButtonInteractionEvent event) {
        AudioChannelChecksHelper helper = new AudioChannelChecksHelper(event);

        if (!helper.inSameChannelWithBot()) {
            replyTextWithDelete(TextReplies.MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT, true, event);
            return;
        }

        MessageEmbed embed = playerManager.setOnLoop(event.getGuild());
        replyEmbedWithDelete(embed, false, event);
    }

    public void changeStatus(boolean isOnLoop) {
        if (isOnLoop) {
            super.setButton(button.withEmoji(LOOP_ON));
        } else {
            super.setButton(button.withEmoji(LOOP_OFF));
        }
    }
}
