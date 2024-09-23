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

import static com.discordbot.helpers.EmbedReplies.*;
import static com.discordbot.helpers.EventReplyHelper.*;
import static com.discordbot.helpers.TextReplies.*;

public class PreviousButton extends AbstractButton {

    public PreviousButton() {
        super(Button.primary("previous", MyEmojis.PREVIOUS));
    }


    @Override
    public void action(ButtonInteractionEvent event) {
        AudioChannelChecksHelper helper = new AudioChannelChecksHelper(event);
        Guild eventGuild = event.getGuild();
        boolean previousTrackExists = playerManager.previousTrackExists(eventGuild);

        if (!helper.inSameChannelWithBot()) {
            replyTextWithDelete(MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT, true, event);

            return;
        }

        if (!playerManager.isPlaying(eventGuild) && !previousTrackExists) {
            replyTextWithDelete(NOTHING_HAS_BEEN_PLAYED_YET, true, event);

            return;
        }

        if (!playerManager.previousTrackExists(eventGuild)) {
            replyTextWithDelete(NO_PREVIOUS_TRACK, true, event);

            return;
        }

        event.deferReply().queue();

        playerManager.playPrevious(eventGuild, event);
    }
}
