package com.discordbot.buttons;

import com.discordbot.AbstractButton;
import com.discordbot.helpers.AudioChannelChecksHelper;
import com.discordbot.ButtonManager;
import com.discordbot.helpers.MyEmojis;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import static com.discordbot.helpers.EventReplyHelper.*;
import static com.discordbot.helpers.TextReplies.MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT;

public class PausePlay extends AbstractButton {
    private final static UnicodeEmoji RESUME = MyEmojis.PLAY;
    private final static UnicodeEmoji PAUSE = MyEmojis.PAUSE;

    public PausePlay() {
        super(Button.primary("pauseplay", PAUSE));
    }

    @Override
    public void action(ButtonInteractionEvent event) {
        Guild eventGuild = event.getGuild();
        event.deferReply(true).queue();

        AudioChannelChecksHelper helper = new AudioChannelChecksHelper(event);

        if (!helper.inSameChannelWithBot()) {
            replyHookTextWithDelete(MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT, event);

            return;
        }

        boolean isPaused = playerManager
                .getGuildMusicManager(eventGuild)
                .getScheduler()
                .isPlayerPaused();

        MessageEmbed messageEmbed = playerManager.setPausedPlayer(eventGuild, !isPaused);

        replyHookEmbedWithDelete(messageEmbed, event);
    }

    public void changeStatus(boolean isPaused) {
        if (isPaused) {
            super.setButton(button.withEmoji(RESUME));
        } else {
            super.setButton(button.withEmoji(PAUSE));
        }
    }
}
