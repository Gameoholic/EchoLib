package com.github.gameoholic.echolib.maps;

public interface MapWriter {

    /**
     Starts downloading the map. It's recommended to run this asynchronously, as it can lag the server for large map sizes.
     */
    void download();
}
