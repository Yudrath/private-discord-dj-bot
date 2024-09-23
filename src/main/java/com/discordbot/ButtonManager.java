package com.discordbot;

import com.discordbot.listeners.ButtonListener;
import com.discordbot.buttons.*;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;

import java.util.ArrayList;
import java.util.List;

public final class ButtonManager {
    private static final int SONG_SELECTORS_AMOUNT = 5;
    private static final ButtonListener BUTTON_LISTENER = ButtonListener.get();

    private static final List<SongSelector> songSelectors = new ArrayList<>();
    private static final List<PlatformSelector> platformSelectors = new ArrayList<>();
    private static CloseSearchResultButton closeSearchResultButton;

    private static PausePlay pausePlay;
    private static SkipButton skipButton;
    private static LoopButton loopButton;
    private static StopButton stopButton;
    private static PreviousButton previousButton;
    private static RestartButton restartButton;

    private static NextPage nextPageButton;
    private static PreviousPage previousPageButton;
    private static CloseQueueButton closeQueueButton;
    private static ClearQueueButton clearQueueButton;


    public static void createButtons() {
        createSongSelectors();
        createPlatformSelectors();
        createCloseSearchResultButton();
        createPlayerControls();
        creatingQueueButtons();
    }

    public enum Platform {
        YOUTUBE ("yt", "YouTube", "ytsearch:"),
        SOUNDCLOUD ("sc", "Soundcloud", "scsearch:"),
        SPOTIFY("sp", "Spotify", "spsearch:");

        private String tag;
        private String name;
        private String prefix;

        private Platform(String tag, String name, String prefix) {
            this.tag = tag;
            this.name = name;
            this.prefix = prefix;
        }

        public String getTag() {
            return tag;
        }

        public String getName() {
            return name;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    private static void creatingQueueButtons() {
        nextPageButton = new NextPage();
        BUTTON_LISTENER.addButton(nextPageButton);

        previousPageButton = new PreviousPage();
        BUTTON_LISTENER.addButton(previousPageButton);

        closeQueueButton = new CloseQueueButton();
        BUTTON_LISTENER.addButton(closeQueueButton);

        clearQueueButton = new ClearQueueButton();
        BUTTON_LISTENER.addButton(clearQueueButton);
    }

    private static void createCloseSearchResultButton() {
        closeSearchResultButton = new CloseSearchResultButton();
        BUTTON_LISTENER.addButton(closeSearchResultButton);
    }

    private static void createSongSelectors() {
        int j = 1;

        while (j <= SONG_SELECTORS_AMOUNT) {
            String numberAsString = String.valueOf(j);
            SongSelector selector = new SongSelector("song-selector-" + numberAsString, numberAsString);
            BUTTON_LISTENER.addButton(selector);
            songSelectors.add(selector);
            j++;
        }
    }

    private static void createPlatformSelectors() {
        for (Platform p : Platform.values()) {
            PlatformSelector platformSelector = new PlatformSelector(p);
            BUTTON_LISTENER.addButton(platformSelector);
            platformSelectors.add(platformSelector);
        }
    }

    private static void createPlayerControls() {
        createPausePlayButton();
        createSkipButton();
        createLoopButton();
        createStopButton();
        createPreviousButton();
        createRestartButton();
    }

    private static void createRestartButton() {
        restartButton = new RestartButton();
        BUTTON_LISTENER.addButton(restartButton);
    }

    private static void createPreviousButton() {
        previousButton = new PreviousButton();
        BUTTON_LISTENER.addButton(previousButton);
    }

    private static void createStopButton() {
        stopButton = new StopButton();
        BUTTON_LISTENER.addButton(stopButton);
    }

    private static void createPausePlayButton() {
        pausePlay = new PausePlay();
        BUTTON_LISTENER.addButton(pausePlay);
    }

    private static void createSkipButton() {
        skipButton = new SkipButton();
        BUTTON_LISTENER.addButton(skipButton);
    }

    private static void createLoopButton() {
        loopButton = new LoopButton();
        BUTTON_LISTENER.addButton(loopButton);
    }

    public static List<LayoutComponent> getSearchButtons(int buttonAmount) {
        List<LayoutComponent> layoutComponents = new ArrayList<>();
        layoutComponents.add(ActionRow.of(getSongSelectorsAsItemComponents(buttonAmount)));
        layoutComponents.add(ActionRow.of(getPlatformSelectorsAsItemComponents()));

        return layoutComponents;
    }

    public static List<ItemComponent> getSongSelectorsAsItemComponents(int buttonAmount) {
        List<ItemComponent> itemComponents = new ArrayList<>();

        for (int k = 0; k < buttonAmount; k++) {
            itemComponents.add(songSelectors.get(k).getButton());
        }

        return itemComponents;
    }

    public static List<ItemComponent> getPlatformSelectorsAsItemComponents() {
        List<ItemComponent> itemComponents = new ArrayList<>();

        for (PlatformSelector selector : platformSelectors) {
            itemComponents.add(selector.getButton());
        }

        return itemComponents;
    }

    public static List<LayoutComponent> getPlayerControls() {
        List<LayoutComponent> layoutComponents = new ArrayList<>();
        List<ItemComponent> itemComponents = new ArrayList<>();

        itemComponents.add(loopButton.getButton());
        itemComponents.add(previousButton.getButton());
        itemComponents.add(pausePlay.getButton());
        itemComponents.add(skipButton.getButton());
        itemComponents.add(restartButton.getButton());

        layoutComponents.add(ActionRow.of(itemComponents));
        layoutComponents.add(ActionRow.of(stopButton.getButton()));

        return layoutComponents;
    }

    public static List<ItemComponent> getQueueButtons() {
        List<ItemComponent> buttons = new ArrayList<>();
        buttons.add(previousPageButton.getButton());
        buttons.add(nextPageButton.getButton());
        buttons.add(closeQueueButton.getButton());
        buttons.add(clearQueueButton.getButton());

        return buttons;
    }

    public static List<ItemComponent> getQueueButtonsNoArrows() {
        List<ItemComponent> buttons = new ArrayList<>();
        buttons.add(closeQueueButton.getButton());
        buttons.add(clearQueueButton.getButton());

        return buttons;
    }

    public static PausePlay getPausePlay() {
        return pausePlay;
    }

    public static NextPage getNextPageButton() {
        return nextPageButton;
    }

    public static PreviousPage getPreviousPageButton() {
        return previousPageButton;
    }

    public static LoopButton getLoopButton() {
        return loopButton;
    }

    public static ItemComponent getCloseSearchResultButtonAsItemComponent() {
        return (ItemComponent) closeSearchResultButton.getButton();
    }
}
