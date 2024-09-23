package com.discordbot.buttons;

import com.discordbot.*;
import com.discordbot.helpers.AudioChannelChecksHelper;
import com.discordbot.helpers.MyEmojis;
import com.discordbot.helpers.TextReplies;
import com.discordbot.helpers.EmbedReplies;
import com.discordbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import static com.discordbot.helpers.EventReplyHelper.*;
import static com.discordbot.helpers.TextReplies.*;

public class RestartButton extends AbstractButton {
    public RestartButton() {
        super(Button.primary("restart", MyEmojis.RESTART));
    }

    @Override
    public void action(ButtonInteractionEvent event) {
        AudioChannelChecksHelper helper = new AudioChannelChecksHelper(event);
        PlayerManager playerManager = PlayerManager.get();
        Guild eventGuild = event.getGuild();

        if (!playerManager.isPlaying(eventGuild)) {
            replyTextWithDelete(NOTHING_IS_PLAYING, true, event);

            return;
        }

        if (!helper.inSameChannelWithBot()) {
            replyTextWithDelete(MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT, true, event);

            return;
        }

        event.deferReply().queue();

        replyHookEmbedWithDelete(EmbedReplies.trackRestartedEmbed(), event);

        playerManager.restartTrack(eventGuild);
    }
}
