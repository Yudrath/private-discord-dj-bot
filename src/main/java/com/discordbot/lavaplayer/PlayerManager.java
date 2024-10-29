package com.discordbot.lavaplayer;

import com.discordbot.ButtonManager;
import com.discordbot.buttons.NextPage;
import com.discordbot.buttons.PreviousPage;
import com.discordbot.helpers.MyEmojis;
import com.github.topi314.lavasrc.mirror.DefaultMirroringAudioTrackResolver;
import com.github.topi314.lavasrc.spotify.SpotifySourceManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.discordbot.helpers.EmbedReplies.*;
import static com.discordbot.helpers.EventReplyHelper.*;

public class PlayerManager {

    private static final int SEARCH_QUERY_RESULT_LIMIT = 5;
    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> guildMusicManagers = new HashMap<>();
    private final AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

    private static final String SPOTIFY_CLIENT_ID = "";
    private static final String SPOTIFY_CLIENT_SECRET = "";
    private static final String SPOTIFY_COUNTRY_CODE = "";
    private static final String SPOTIFY_SPDC = "";


    private PlayerManager() {
        String[] providers = new String[4];
        providers[0] = "ytsearch:\"%ISRC%\"";
        providers[1] = "ytsearch:%QUERY%";

        SpotifySourceManager spotify = new SpotifySourceManager(
                SPOTIFY_CLIENT_ID,
                SPOTIFY_CLIENT_SECRET,
                SPOTIFY_SPDC,
                SPOTIFY_COUNTRY_CODE,
                (s) -> audioPlayerManager,
                new DefaultMirroringAudioTrackResolver(providers));

        dev.lavalink.youtube.YoutubeAudioSourceManager ytSourceManager = new dev.lavalink.youtube.YoutubeAudioSourceManager();
        audioPlayerManager.registerSourceManager(ytSourceManager);

        audioPlayerManager.registerSourceManager(spotify);

        AudioSourceManagers.registerRemoteSources(audioPlayerManager, com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);

    }

    public static PlayerManager get() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }

    public GuildMusicManager getGuildMusicManager(Guild guild) {
        return guildMusicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager musicManager = new GuildMusicManager(audioPlayerManager);

            guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

            return musicManager;
        });
    }

    //Slash command's play method
    public void play(Guild guild, String trackURL, SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = getGuildMusicManager(guild);
        TrackScheduler scheduler = musicManager.getScheduler();
        TextChannel textChannel = event.getChannel().asTextChannel();

        audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack audioTrack) {

                if (scheduler.isPlaying()) {
                    event.getHook()
                            .sendMessageEmbeds(queuedTrackEmbed(audioTrack))
                            .queue();
                } else {
                    event.getHook()
                            .sendMessageEmbeds(playingTrackEmbed(audioTrack))
                            .setComponents(ButtonManager.getPlayerControls())
                            .queue((message -> scheduler.setMessageReference(message)));
                }

                scheduler.queue(new QueuedTrack(audioTrack, textChannel));
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                if (!scheduler.isPlaying()) {
                    AudioTrack firstTrack = audioPlaylist.getTracks().get(0);

                    event.getHook()
                            .sendMessageEmbeds(playingTrackEmbed(firstTrack))
                            .setComponents(ButtonManager.getPlayerControls())
                            .queue((message ->
                                scheduler.setMessageReference(message)));
                }

                for (AudioTrack track : audioPlaylist.getTracks()) {
                    scheduler.queue(new QueuedTrack(track, textChannel));
                }

                event.getHook().sendMessageEmbeds(playlistLoadedEmbed(audioPlaylist, trackURL)).queue();
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }

    //SongSelector's play method
    public void play(Guild guild, String trackURL, ButtonInteractionEvent event) {
        GuildMusicManager musicManager = getGuildMusicManager(guild);
        TrackScheduler scheduler = musicManager.getScheduler();

        event.getMessage().delete().queue(); //Deleting the original message

        audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                if (scheduler.isPlaying()) {
                    event.getHook().sendMessageEmbeds(queuedTrackEmbed(audioTrack)).queue();
                } else {
                    event.getHook()
                            .sendMessageEmbeds(playingTrackEmbed(audioTrack))
                            .setComponents(ButtonManager.getPlayerControls())
                            .queue((message -> scheduler.setMessageReference(message)));
                }

                TextChannel textChannel = event.getChannel().asTextChannel();

                scheduler.queue(new QueuedTrack(audioTrack, textChannel));
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {}

            @Override
            public void noMatches() {}

            @Override
            public void loadFailed(FriendlyException e) {}
        });
    }

    public void searchQuery(Guild guild, String query, GenericInteractionCreateEvent event, ButtonManager.Platform platform) {
        String prefixedQuery = platform.getPrefix() + query;
        String platformName = platform.getName();

        GuildMusicManager musicManager = getGuildMusicManager(guild);
        List<AudioTrack> foundTracks = musicManager.getSearchQueryResult(); //I could also create a new list and set it over the other one, instead of emptying this one

        audioPlayerManager.loadItemSync(prefixedQuery, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                foundTracks.clear();

                List<AudioTrack> playlistTracks = playlist.getTracks();

                if (playlistTracks.isEmpty()) {
                    replyHookEmbedWithDelete(negativeReplyEmbed("No tracks have been found."), event);
                }

                int foundResults = playlistTracks.size();
                int neededResults = SEARCH_QUERY_RESULT_LIMIT;
                int resultLimit;

                if (foundResults < neededResults) {
                    resultLimit = foundResults;
                } else {
                    resultLimit = neededResults;
                }

                for (int i = 0; i < resultLimit; i++) {
                    foundTracks.add(playlistTracks.get(i));
                }

                //There's a difference in how this method was called. If it's a slash command, we SEND the initial embed, if it's
                //called through the platform search buttons, we need to EDIT the embed
                if (event instanceof SlashCommandInteractionEvent slashEvent) {
                    slashEvent
                            .getHook()
                            .sendMessageEmbeds(searchResultEmbed(platformName, foundTracks))
                            .setComponents
                                    (ActionRow.of(ButtonManager.getSongSelectorsAsItemComponents(resultLimit)),
                                    ActionRow.of(ButtonManager.getPlatformSelectorsAsItemComponents()),
                                    ActionRow.of(ButtonManager.getCloseSearchResultButtonAsItemComponent()))
                            .queue();
                } else if (event instanceof ButtonInteractionEvent buttonEvent) {
                    buttonEvent
                            .editMessageEmbeds(searchResultEmbed(platformName, foundTracks))
                            .setComponents
                                    (ActionRow.of(ButtonManager.getSongSelectorsAsItemComponents(resultLimit)),
                                    ActionRow.of(ButtonManager.getPlatformSelectorsAsItemComponents()),
                                    ActionRow.of(ButtonManager.getCloseSearchResultButtonAsItemComponent()))
                            .queue();
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {

            }
        });
    }

    public MessageEmbed skipToTrack(Guild guild, int skipTo) {
        GuildMusicManager musicManager = getGuildMusicManager(guild);
        TrackScheduler scheduler = musicManager.getScheduler();

        if (skipTo > scheduler.getQueue().size()) {
            return negativeReplyEmbed("You have given me an invalid track number. There's no song with number " + skipTo);
        }

        MessageEmbed embed = skippedSongEmbed(scheduler);

        scheduler.skipTracks(skipTo);

        return embed;
    }

    public MessageEmbed skip(Guild guild) {
        GuildMusicManager musicManager = getGuildMusicManager(guild);
        TrackScheduler scheduler = musicManager.getScheduler();

        MessageEmbed embed = skippedSongEmbed(scheduler);

        scheduler.nextTrack();

        return embed;
    }

    public MessageEmbed emptyQueue(Guild guild) {
        TrackScheduler scheduler = getGuildMusicManager(guild).getScheduler();
        QueueList queueList = getGuildMusicManager(guild).getQueueList();

        boolean queueIsEmpty = scheduler.isQueueEmpty();

        if (queueIsEmpty) {
            return negativeReplyEmbed("There's nothing in the queue.");
        }

        MessageEmbed embed = queueEmptiedEmbed(scheduler);
        scheduler.emptyQueue();

        if (queueList != null) {
            queueList.getQueueListMessageReference().deleteMessage();
        }

        return embed;
    }

    public void showQueue(Guild guild, SlashCommandInteractionEvent event) {
        GuildMusicManager manager = getGuildMusicManager(guild);
        TrackScheduler scheduler = manager.getScheduler();

        if (scheduler.isQueueEmpty()) {
            replyHookEmbedWithDelete(negativeReplyEmbed("The queue is empty."), event);

            return;
        }

        replyHookEmbedWithDelete(positiveReplyEmbed("Displaying queue"), event);

        manager.displayQueue(event.getMessageChannel());
    }

    public void scrollQueue(Guild guild, ButtonInteractionEvent event) {
        QueueList queueList = getGuildMusicManager(guild).getQueueList();
        int pageNumber = queueList.getPageNumber();
        int pageAmount = queueList.getPageAmount();

        NextPage nextPageButton = ButtonManager.getNextPageButton();
        PreviousPage previousPageButton = ButtonManager.getPreviousPageButton();

        if ((pageNumber + 1) == pageAmount) { // pageNumber + 1, because pageNumber by default is 0, not 1
            nextPageButton.disableButton();
        } else if (nextPageButton.isDisabled()) {
            nextPageButton.enableButton();
        }

        if (pageNumber == 0) {
            previousPageButton.disableButton();
        } else if (previousPageButton.isDisabled()) {
            previousPageButton.enableButton();
        }

        event
                .editMessageEmbeds(queueList.scrollPage())
                .setComponents(ActionRow.of(ButtonManager.getQueueButtons()))
                .queue();
    }

    public MessageEmbed setPausedPlayer(Guild guild, boolean isPaused) {
        TrackScheduler scheduler = getGuildMusicManager(guild).getScheduler();

        scheduler.setPausedPlayer(isPaused);

        return pauseEmbed(scheduler, isPaused);
    }

    public MessageEmbed setOnLoop(Guild guild) {
        TrackScheduler scheduler = getGuildMusicManager(guild).getScheduler();
        boolean isOnLoop = scheduler.getOnLoop();
        scheduler.setOnLoop(!isOnLoop);

        return setOnLoopEmbed(!isOnLoop);
    }

    public MessageEmbed stopPlayer(Guild guild) {
        TrackScheduler scheduler = getGuildMusicManager(guild).getScheduler();
        boolean queueIsEmpty = scheduler.isQueueEmpty();
        MessageEmbed embed = stopPlayerEmbed(scheduler);

        if (!queueIsEmpty) {
            scheduler.emptyQueue();
        }

        scheduler.stopTrack();

        return embed;
    }

    public void playPrevious(Guild guild, GenericInteractionCreateEvent event) {
        TrackScheduler scheduler = getGuildMusicManager(guild).getScheduler();

        if (scheduler.isNowPlayingPrevious()) {
            replyHookEmbedWithDelete(negativeReplyEmbed("Already playing the previous track."), event);
            return;
        }

        replyHookEmbedWithDelete(positiveReplyEmbed(MyEmojis.PREVIOUS.getFormatted() + " Playing the previous track"), event);

        scheduler.playPreviousTrack();
    }

    public void restartTrack(Guild guild) {
        TrackScheduler scheduler = getGuildMusicManager(guild).getScheduler();
        scheduler.restartCurrentTrack();
    }

    public void pauseOnLeave(Guild guild) {
        TrackScheduler scheduler = getGuildMusicManager(guild).getScheduler();

        if (scheduler.isPlaying()) {
            scheduler.setPausedPlayer(true);
            scheduler.getCurrentTrackMessage().editMessageComponents(ButtonManager.getPlayerControls()).queue();
        }
    }

    public boolean isPlaying(Guild guild) {
        return getGuildMusicManager(guild).getScheduler().isPlaying();
    }

    public boolean isPlayerPaused(Guild guild) {
        return getGuildMusicManager(guild).getScheduler().isPlayerPaused();
    }

    public boolean previousTrackExists(Guild guild) {
        TrackScheduler scheduler = getGuildMusicManager(guild).getScheduler();

        return scheduler.previousTrackIsSet();
    }
}
