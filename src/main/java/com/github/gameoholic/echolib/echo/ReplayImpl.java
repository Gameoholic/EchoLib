package com.github.gameoholic.echolib.echo;

import com.github.gameoholic.echolib.EchoLib;
import com.github.gameoholic.echolib.Replay;

import java.util.UUID;

public class ReplayImpl implements Replay {
    private final UUID id;
    private final int version; //Replay API version. Will update only when Replay code is changed.
    private boolean isRecording;
    private String foo;
    private String bar;
    public ReplayImpl(String foo, String bar) {
        this.foo = foo;
        this.bar = bar;

        id = UUID.randomUUID();
        version = EchoLib.replayAPIVersion;
        isRecording = false;
    }

    @Override
    public void startRecording() {
        if (isRecording)
            return;
        isRecording = true;
    }

    @Override
    public UUID getId() {
        return id;
    }
    @Override
    public int getVersion() {
        return version;
    }
}
