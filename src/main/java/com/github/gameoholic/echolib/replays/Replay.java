package com.github.gameoholic.echolib.replays;

import java.util.UUID;

public interface Replay {


    void startRecording();
    /**
     * Returns the Replay's ID.
     * <p>
     *
     * @return The Replay's ID.
     */
    UUID getId();

    /**
     * Returns the Replay API version.
     * <p>
     * The Replay API version only changes with releases that break replay viewal for replays on older versions.
     *
     * @return The Replay's Replay API version.
     */
    int getVersion();

}
