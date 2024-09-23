package com.discordbot.listeners;

import com.discordbot.AbstractButton;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ButtonListener extends ListenerAdapter {

    private List<AbstractButton> buttons = new ArrayList<>();
    private static ButtonListener INSTANCE;

    public static ButtonListener get() {
        if (INSTANCE == null) {
            INSTANCE = new ButtonListener();
        }

        return INSTANCE;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        for (AbstractButton button : buttons) {
            if (event.getComponentId().equals(button.getComponentId())) {
                button.action(event);
            }
        }
    }

    public void addButton(AbstractButton button) {
        buttons.add(button);
    }
}
