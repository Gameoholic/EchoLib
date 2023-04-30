package com.github.gameoholic.echolib;

import com.github.gameoholic.echolib.echo.MapDownloaderImpl;
import org.bukkit.util.Vector;


public class MapDownloaderBuilder {
    //Required attributes:
    private Vector cornerCoords;
    private Vector size;

    //Optional map download parameters:
    private Boolean downloadBlockBiome;

    public MapDownloaderBuilder(Vector cornerCoords, Vector size) {
        this.cornerCoords = cornerCoords;
        this.size = size;
    }

    public MapDownloaderBuilder withDownloadBlockBiome(Boolean downloadBlockBiome) {
        this.downloadBlockBiome = downloadBlockBiome;
        return this;
    }

    public MapDownloader build() {
        MapDownloader mapDownloader = new MapDownloaderImpl(cornerCoords, size, downloadBlockBiome);
        validateMapDownloaderObject(mapDownloader);
        return mapDownloader;
    }

    private void validateMapDownloaderObject(MapDownloader mapDownloader) {
        //Do some basic validations to check
        //if user object does not break any assumption of system
    }
}
