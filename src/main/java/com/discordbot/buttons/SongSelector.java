package com.discordbot.buttons;

import com.discordbot.AbstractButton;
import com.discordbot.helpers.AudioChannelChecksHelper;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

import static com.discordbot.helpers.EventReplyHelper.replyTextWithDelete;
import static com.discordbot.helpers.TextReplies.MEMBER_NOT_IN_AUDIO_CHANNEL;
import static com.discordbot.helpers.TextReplies.MEMBER_NOT_IN_SAME_AUDIO_CHANNEL_AS_BOT;

public class SongSelector extends AbstractButton {

    public SongSelector(String componentID, String label) {
        super(componentID, label, Button.primary(componentID, label));
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

        event.deferReply().queue();

        List<AudioTrack> searchResultList = playerManager.getGuildMusicManager(eventGuild).getSearchQueryResult();
        int trackNumber = Integer.parseInt(label);
        String trackURI = searchResultList.get(trackNumber - 1).getInfo().uri; //the -1 is because the 1st index of the list is 0 and the label of the 1st button is 1

        playerManager.play(eventGuild, trackURI, event);
    }
}
