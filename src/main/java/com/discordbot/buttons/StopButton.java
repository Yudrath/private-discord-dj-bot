package com.discordbot.buttons;

import com.discordbot.*;
import com.discordbot.helpers.AudioChannelChecksHelper;
import com.discordbot.helpers.MyEmojis;
import com.discordbot.helpers.TextReplies;
import com.discordbot.helpers.EmbedReplies;
import com.discordbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import static com.discordbot.helpers.EventReplyHelper.*;
import static com.discordbot.helpers.TextReplies.*;

public class StopButton extends AbstractButton {

    public StopButton() {
        super(Button.danger("stop", MyEmojis.STOP));
    }

    @Override
    public void action(ButtonInteractionEvent event) {
        AudioChannelChecksHelper helper = new AudioChannelChecksHelper(event);
        Guild guild = event.getGuild();

        if (!playerManager.isPlaying(guild)) {
            replyTextWithDelete(NOTHING_IS_PLAYING, true, event);

            return;
        }

        if (!helper.inSameChannelWithBot()) {
            replyTextWithDelete(MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT, true, event);

            return;
        }

        MessageEmbed embed = playerManager.stopPlayer(guild);
        replyEmbedWithDelete(embed, false, event);
        helper.botCloseAudioConnection();
    }
}
