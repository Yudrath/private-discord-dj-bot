package com.discordbot.buttons;

import com.discordbot.AbstractButton;
import com.discordbot.helpers.AudioChannelChecksHelper;
import com.discordbot.helpers.MyEmojis;
import com.discordbot.helpers.TextReplies;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import static com.discordbot.helpers.EventReplyHelper.*;
import static com.discordbot.helpers.TextReplies.MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT;
import static com.discordbot.helpers.TextReplies.NOTHING_IS_PLAYING;

public class SkipButton extends AbstractButton {

    public SkipButton() {
        super(Button.primary("skip", MyEmojis.SKIP));
    }

    @Override
    public void action(ButtonInteractionEvent event) {
        AudioChannelChecksHelper helper = new AudioChannelChecksHelper(event);
        Guild eventGuild = event.getGuild();

        //This check is irrelevant, because the skip button is only visible, when something is playing already
        if (!playerManager.isPlaying(eventGuild)) {
            replyTextWithDelete(NOTHING_IS_PLAYING, true, event);

            return;
        }

        if (!helper.inSameChannelWithBot()) {
            replyTextWithDelete(MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT, true, event);
            return;
        }

        MessageEmbed embed = playerManager.skip(event.getGuild());
        replyEmbedWithDelete(embed, false, event);
    }
}
