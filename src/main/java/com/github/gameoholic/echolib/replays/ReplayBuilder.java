package com.github.gameoholic.echolib.replays;

import com.github.gameoholic.echolib.echo.replays.ReplayImpl;

public class ReplayBuilder {
    //Required attributes:
    private String foo;
    //Optional attributes:
    private String bar;

    public ReplayBuilder(String foo) {
        this.foo = foo;
    }

    public ReplayBuilder withBar(String bar) {
        this.bar = bar;
        return this;
    }

    public Replay build() {
        Replay replay = new ReplayImpl(foo, bar);
        validateReplayObject(replay);
        return replay;
    }

    private void validateReplayObject(Replay replay) {
        //Do some basic validations to check
        //if user object does not break any assumption of system
    }
}
