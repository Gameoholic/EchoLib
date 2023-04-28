package com.github.gameoholic.echolib.echo;

import com.github.gameoholic.echolib.EchoLib;
import com.github.gameoholic.echolib.Replay;
import com.github.gameoholic.echolib.ReplayBuilder;

import java.util.UUID;

public class EchoReplay implements Replay {
    private final UUID id;
    private final int version; //Replay API version. Will update only when Replay code is changed.
    private String foo;
    private String bar;
    public EchoReplay(ReplayBuilder builder) {
        this.foo = builder.foo;
        this.bar = builder.bar;

        id = UUID.randomUUID();
        version = EchoLib.replayAPIVersion;
    }




}
