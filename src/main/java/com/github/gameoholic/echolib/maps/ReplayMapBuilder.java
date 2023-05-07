package com.github.gameoholic.echolib.maps;

import com.github.gameoholic.echolib.echo.maps.ReplayMapImpl;

import java.util.UUID;

public class ReplayMapBuilder {
    //Required attributes:
    private UUID uuid;


    public ReplayMapBuilder(UUID uuid) {
        this.uuid = uuid;
    }



    public ReplayMap build() {
        ReplayMap mapReader = new ReplayMapImpl(uuid);
        validateMapDownloaderObject(mapReader);
        return mapReader;
    }

    private void validateMapDownloaderObject(ReplayMap mapReader) {

    }
}
