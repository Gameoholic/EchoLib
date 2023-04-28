package com.github.gameoholic.echolib;

import com.github.gameoholic.echolib.echo.EchoReplay;

import java.util.UUID;

public class ReplayBuilder {
    //Required attributes:
    public String foo;
    //Optional attributes:
    public String bar;

    public ReplayBuilder(String foo) {
        this.foo = foo;
    }

    public ReplayBuilder withBar(String bar) {
        this.bar = bar;
        return this;
    }

    public Replay build() {
        Replay replay = new EchoReplay(this);
        validateReplayObject(replay);
        return replay;
    }

    private void validateReplayObject(Replay replay) {
        //Do some basic validations to check
        //if user object does not break any assumption of system
    }
}
