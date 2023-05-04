package com.github.gameoholic.echolib;

public interface MapDownloader{

    /**
     Starts downloading the map. It's recommended to run this asynchronously, as it can lag the server for large map sizes.
     */
    void download();
}
