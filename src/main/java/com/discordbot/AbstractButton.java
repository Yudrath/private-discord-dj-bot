package com.discordbot;

import com.discordbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public abstract class AbstractButton {
    protected String componentId;
    protected String label;
    protected Button button;
    protected final PlayerManager playerManager;

    public AbstractButton(Button button) {
        this.componentId = button.getId();
        this.label = button.getLabel();
        this.button = button;
        this.playerManager = PlayerManager.get();
    }

    //I need this constructor for the SongSelectors
    public AbstractButton(String componentId, String label, Button button) {
        this.componentId = componentId;
        this.label = label;
        this.button = button;
        this.playerManager = PlayerManager.get();
    }

    public String getLabel() {
        return label;
    }

    public String getComponentId() {
        return componentId;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
        this.componentId = button.getId();
        this.label = button.getLabel();
    }

    public abstract void action(ButtonInteractionEvent event);

    public void disableButton() {
        button = button.asDisabled();
    }

    public void enableButton() {
        button = button.asEnabled();
    }

    public boolean isDisabled() {
        return button.isDisabled();
    }
}
