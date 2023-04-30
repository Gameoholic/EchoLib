package com.github.gameoholic.echolib.echo;

import com.github.gameoholic.echolib.MapDownloader;
import org.bukkit.util.Vector;

public class MapDownloaderImpl implements MapDownloader {
    private Vector cornerCoords;
    private Vector size;
    private Boolean downloadBlockBiome;
    public MapDownloaderImpl(Vector cornerCoords, Vector size, boolean downloadBlockBiome) {
        this.cornerCoords = cornerCoords;
        this.size = size;
        this.downloadBlockBiome = downloadBlockBiome;
    }

    @Override
    public void download() {

    }
}
