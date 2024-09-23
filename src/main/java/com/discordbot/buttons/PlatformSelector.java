package com.discordbot.buttons;

import com.discordbot.AbstractButton;
import com.discordbot.helpers.AudioChannelChecksHelper;
import com.discordbot.ButtonManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import static com.discordbot.helpers.EventReplyHelper.*;
import static com.discordbot.helpers.TextReplies.MEMBER_NOT_IN_AUDIO_CHANNEL;
import static com.discordbot.helpers.TextReplies.MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT;

public class PlatformSelector extends AbstractButton {
    private final ButtonManager.Platform platform;

    public PlatformSelector(ButtonManager.Platform platform) {
        super(Button.success(platform.getTag(), platform.getName()));
        this.platform = platform;
    }

    @Override
    public void action(ButtonInteractionEvent event) {
        Guild eventGuild = event.getGuild();
        AudioChannelChecksHelper helper = new AudioChannelChecksHelper(event);

        if (!helper.isMemberInChannel()) {
            replyTextWithDelete(MEMBER_NOT_IN_AUDIO_CHANNEL, true, event);

            return;
        }

        if (!helper.isBotInChannel()) {
            helper.botOpenAudioConnection();
        } else if (!helper.inSameChannelWithBot()) {
            replyTextWithDelete(MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT, true, event);

            return;
        }

        String lastSearchQuery = playerManager.getGuildMusicManager(eventGuild).getLastSearchQuery();

        playerManager.searchQuery(eventGuild, lastSearchQuery, event, platform);
    }
}
