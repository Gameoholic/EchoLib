package com.github.gameoholic.echolib;

import com.github.gameoholic.echolib.echo.MapDownloaderImpl;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.nio.charset.StandardCharsets;


public class MapDownloaderBuilder {
    //Required attributes:
    private World world;
    private Vector cornerCoords;
    private Vector size;
    //Optional attributes:
    private String name;
    private String description;

    //Optional map download parameters:
    private Boolean downloadBlockBiome;

    /**
     A builder class for creating a MapDownloader object, which represents a downloadable area of a Minecraft world.

     @param name The name of the map to download, encoded in UTF-8.
     @param description A description of the map to download, encoded in UTF-8.
     @param world The world to download from.
     @param cornerCoords The coordinates of the south-east-bottom corner of the area to download.
     @param size The size of the area to download.

     @note Hardcoded limitations, not following them will result in undefined behaviour:
      * Name and description are encoded in UTF-8.
      * Name and description length must be between 1-65536 bytes.
     */
    public MapDownloaderBuilder(String name, String description, World world, Vector cornerCoords, Vector size) {
        this.name = name;
        this.description = description;
        this.world = world;
        this.cornerCoords = cornerCoords;
        this.size = size;
        //Optional defaults:
        this.downloadBlockBiome = false;
    }

    /**
     Sets whether to download the biome data for each block in the selected area.
     Default is false.
    */
     public MapDownloaderBuilder withDownloadBlockBiome(Boolean downloadBlockBiome) {
        this.downloadBlockBiome = downloadBlockBiome;
        return this;
     }

    public MapDownloader build() {
        MapDownloader mapDownloader = new MapDownloaderImpl(name, description, world, cornerCoords, size, downloadBlockBiome);
        validateMapDownloaderObject(mapDownloader);
        return mapDownloader;
    }

    private void validateMapDownloaderObject(MapDownloader mapDownloader) {
        if (name == null || name.length() < 1 || name.length() > 65536)
            throw new IllegalArgumentException("Map name must be between 1-65536 characters.");
        if (description == null || description.length() < 1 || description.length() > 65536)
            throw new IllegalArgumentException("Map description must be between 1-65536 characters.");
        if (world == null)
            throw new IllegalArgumentException("World must not be null.");
        if (cornerCoords == null)
            throw new IllegalArgumentException("Corner coords must not be null.");
        if (size == null)
            throw new IllegalArgumentException("Size must not be null.");
        if (!StandardCharsets.UTF_8.newEncoder().canEncode(name))
            throw new IllegalArgumentException("Map name must be encoded in UTF-8.");
        if (!StandardCharsets.UTF_8.newEncoder().canEncode(description))
            throw new IllegalArgumentException("Map description must be encoded in UTF-8.");
    }
}
