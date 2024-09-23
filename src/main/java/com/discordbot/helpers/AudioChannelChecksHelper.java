package com.discordbot.helpers;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.concurrent.TimeUnit;

public class AudioChannelChecksHelper {
    private final Guild guild;
    private final Member self;
    private final Member member;
    private final GuildVoiceState selfVoiceState;
    private final GuildVoiceState memberVoiceState;

    public AudioChannelChecksHelper(GenericInteractionCreateEvent event) {
        guild = event.getGuild();
        self = event.getGuild().getSelfMember();
        member = event.getMember();
        selfVoiceState = self.getVoiceState();
        memberVoiceState = member.getVoiceState();
    }

    public boolean isMemberInChannel() {
        return memberVoiceState.inAudioChannel();
    }

    public boolean isBotInChannel() {
        return selfVoiceState.inAudioChannel();
    }

    public boolean inSameChannelWithBot() {
        return memberVoiceState.getChannel() == selfVoiceState.getChannel();
    }

    public void botOpenAudioConnection() {
        guild.getAudioManager().openAudioConnection(memberVoiceState.getChannel()); //If there's a bug with /play, look here
    }

    public void botCloseAudioConnection() {
        guild.getAudioManager().closeAudioConnection();
    }

    public Channel getSelfChannel() {
        return selfVoiceState.getChannel();
    }

    public String getMemberChannelName() {
        return memberVoiceState.getChannel().getName();
    }

    public String getSelfChannelName() {
        return selfVoiceState.getChannel().getName();
    }
}
