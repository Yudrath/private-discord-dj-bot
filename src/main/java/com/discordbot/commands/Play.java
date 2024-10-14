package com.discordbot.commands;

import com.discordbot.helpers.AudioChannelChecksHelper;
import com.discordbot.ButtonManager;
import com.discordbot.ICommand;
import com.discordbot.helpers.EmbedReplies;
import com.discordbot.lavaplayer.GuildMusicManager;
import com.discordbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import static com.discordbot.helpers.EventReplyHelper.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.discordbot.helpers.TextReplies.*;

public class Play implements ICommand {

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Plays a song or a playlist";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "link-or-query", "song/playlist", true));

        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        AudioChannelChecksHelper helper = new AudioChannelChecksHelper(event);
        PlayerManager playerManager = PlayerManager.get();

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

        String userSearch = event.getOption("link-or-query").getAsString();
        Guild eventGuild = event.getGuild();

        if (!isURL(userSearch)) {
            GuildMusicManager eventGuildMusicManager = playerManager.getGuildMusicManager(eventGuild);
            eventGuildMusicManager.setLastSearchQuery(userSearch);

            ButtonManager.Platform defaultSearchPlatform = ButtonManager.Platform.YOUTUBE;

            playerManager.searchQuery(eventGuild, userSearch, event, defaultSearchPlatform);
        } else {
            if(!isValidURL(userSearch)) {
                replyHookEmbedWithDelete(EmbedReplies.negativeReplyEmbed(userSearch + " **is not a valid URL**"), event);

                return;
            }

            playerManager.play(eventGuild, userSearch, event);
        }
    }

    private boolean isURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (URISyntaxException e) {
            return false;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private boolean isValidURL(String url) {
        try {
            URI uri = new URL(url).toURI();
            String domain = uri.getHost();

            System.out.println(domain);

            if (domain.equals("soundcloud.com")
                    || domain.equals("youtu.be")
                    || domain.equals("www.youtube.com")
                    || domain.equals("open.spotify.com")) {

                return true;
            }

            return false;
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();

            return false;
        }
    }
}
