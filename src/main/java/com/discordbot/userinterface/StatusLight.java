package com.discordbot.userinterface;

import com.discordbot.helperbot.HelperBot;
import javafx.scene.shape.Circle;

public class StatusLight {
    private static final int radius = 8;
    private Circle light;

    private boolean switchedOn;

    public StatusLight(boolean isOnline) {
        this.light = new Circle(radius);
        this.switchedOn = isOnline;
        switchOn(switchedOn);
    }

    private void setInitialLightStatus() {
        boolean djIsOnline = HelperBot.isDJOnline();

        if (djIsOnline) {
            light.setId("status-light-green");
        } else {
            light.setId("status-light-red");
        }
    }

    public void switchOn(boolean isOn) {
        if (isOn) {
            light.setId("status-light-green");
        } else {
            light.setId("status-light-red");
        }
    }

    public Circle getLight() {
        return light;
    }
}
