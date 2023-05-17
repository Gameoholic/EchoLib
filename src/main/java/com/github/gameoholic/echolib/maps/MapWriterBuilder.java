package com.github.gameoholic.echolib.maps;

import com.github.gameoholic.echolib.echo.maps.BlockDataType;
import com.github.gameoholic.echolib.echo.maps.MapWriterImpl;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;


public class MapWriterBuilder {
    //Required attributes:
    private World world;
    private Vector cornerCoords;
    private Vector size;
    //Optional attributes:
    private String name;
    private String description;

    //Optional map download parameters:
    private Boolean downloadBlockBiome;
    private Boolean downloadBlockFacing;
    private Boolean downloadStairsData;
    private Boolean downloadRailData;
    private Boolean downloadBedData;
    private Boolean downloadWallData;
    private Boolean downloadBambooData;
    private Boolean downloadSlabData;
    private Boolean downloadFenceData;
    private Boolean downloadFenceGateData;
    private Boolean downloadDoorData;
    private Boolean downloadBisectedData;
    private Boolean downloadAgeableData;
    private Boolean downloadPoweredData;
    private Boolean downloadWaterloggedData;
    private Boolean downloadOpenableData;
    private Boolean downloadAnaloguePowerableData;
    private Boolean downloadAttachableData;
    private Boolean downloadBeehiveData;
    private Boolean downloadBellData;
    private Boolean downloadBigDripleafData;
    private Boolean downloadBubbleColumnData;
    private Boolean downloadCakeData;


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
    public MapWriterBuilder(String name, String description, World world, Vector cornerCoords, Vector size) {
        this.name = name;
        this.description = description;
        this.world = world;
        this.cornerCoords = cornerCoords;
        this.size = size;
        //Optional defaults:
        this.downloadBlockBiome = true;
        this.downloadBlockFacing = true;
        this.downloadStairsData = true;
        this.downloadRailData = true;
        this.downloadBedData = true;
        this.downloadWallData = true;
        this.downloadBambooData = true;
        this.downloadSlabData = true;
        this.downloadFenceData = true;
        this.downloadFenceGateData = true;
        this.downloadDoorData = true;
        this.downloadBisectedData = true;
        this.downloadWaterloggedData = true;
        this.downloadPoweredData = true;
        this.downloadAgeableData = true;
        this.downloadOpenableData = true;
        this.downloadAnaloguePowerableData = false;
        this.downloadAttachableData = true;
        this.downloadBeehiveData = true;
        this.downloadBellData = true;
        this.downloadBigDripleafData = true;
        this.downloadBubbleColumnData = false;
        this.downloadCakeData = true;
    }
    public MapWriter build() {
        HashMap<BlockDataType, Boolean> writeBlockDataArguments = new HashMap<>() {{
            put(BlockDataType.BIOME, downloadBlockBiome);
            put(BlockDataType.FACING, downloadBlockFacing);
            put(BlockDataType.STAIRS_DATA, downloadStairsData);
            put(BlockDataType.BED_DATA, downloadBedData);
            put(BlockDataType.WALL_DATA, downloadWallData);
            put(BlockDataType.BAMBOO_DATA, downloadBambooData);
            put(BlockDataType.SLAB_DATA, downloadSlabData);
            put(BlockDataType.FENCE_GATE_DATA, downloadFenceGateData);
            put(BlockDataType.FENCE_DATA, downloadFenceData);
            put(BlockDataType.RAIL_DATA, downloadRailData);
            put(BlockDataType.BISECTED, downloadBisectedData);
            put(BlockDataType.DOOR_DATA, downloadDoorData);
            put(BlockDataType.WATERLOGGED, downloadWaterloggedData);
            put(BlockDataType.AGEABLE, downloadAgeableData);
            put(BlockDataType.POWERABLE, downloadPoweredData);
            put(BlockDataType.OPENABLE, downloadOpenableData);
            put(BlockDataType.ANALOGUE_POWERABLE, downloadAnaloguePowerableData);
            put(BlockDataType.ATTACHABLE, downloadAttachableData);
            put(BlockDataType.BEEHIVE_DATA, downloadBeehiveData);
            put(BlockDataType.BELL_DATA, downloadBellData);
            put(BlockDataType.BIGDRIPLEAF_DATA, downloadBigDripleafData);
            put(BlockDataType.CAKE_DATA, downloadCakeData);

        }};

        MapWriter mapDownloader = new MapWriterImpl(name, description, world, cornerCoords, size, writeBlockDataArguments);
        validateMapDownloaderObject(mapDownloader);
        return mapDownloader;
    }

    private void validateMapDownloaderObject(MapWriter mapDownloader) {
        if (name == null || name.length() < 1 || name.length() > 65536)
            throw new IllegalArgumentException("Map name must be between 1-65536 characters.");
        if (description == null || description.length() < 1 || description.length() > 65536)
            throw new IllegalArgumentException("Map description must be between 1-65536 characters.");
        if (world == null)
            throw new IllegalArgumentException("World must not be null.");
        if (cornerCoords == null)
            throw new IllegalArgumentException("Corner coords must not be null.");
        if (size == null) //TODO: make sure size is correct and doesn't go out of impossible bounds
            throw new IllegalArgumentException("Size is invalid or null.");
        if (size.getBlockX() > 2048 || size.getBlockY() > 512 || size.getBlockZ() > 2048)
            throw new IllegalArgumentException("Size exceeds limit.");
        if (!StandardCharsets.UTF_8.newEncoder().canEncode(name))
            throw new IllegalArgumentException("Map name must be encoded in UTF-8.");
        if (!StandardCharsets.UTF_8.newEncoder().canEncode(description))
            throw new IllegalArgumentException("Map description must be encoded in UTF-8.");
    }

    /**
     Sets whether to download the biome data for each block in the selected area.
     Default is false.
    */
     public MapWriterBuilder withDownloadBlockBiome(Boolean downloadBlockBiome) {
        this.downloadBlockBiome = downloadBlockBiome;
        return this;
     }
    public MapWriterBuilder withDownloadBlockFace(Boolean downloadBlockFacing) {
        this.downloadBlockFacing = downloadBlockFacing;
        return this;
    }
    public MapWriterBuilder withDownloadBlockShape(Boolean downloadStairsData) {
        this.downloadStairsData = downloadStairsData;
        return this;
    }
    public MapWriterBuilder withDownloadBedData(Boolean downloadBedData) {
        this.downloadBedData = downloadBedData;
        return this;
    }
    public MapWriterBuilder withDownloadWallData(Boolean downloadWallData) {
        this.downloadWallData = downloadWallData;
        return this;
    }
    public MapWriterBuilder withDownloadBambooData(Boolean downloadBambooData) {
        this.downloadBambooData = downloadBambooData;
        return this;
    }
    public MapWriterBuilder withDownloadSlabData(Boolean downloadSlabData) {
        this.downloadSlabData = downloadSlabData;
        return this;
    }
    public MapWriterBuilder withDownloadFenceData(Boolean downloadFenceData) {
        this.downloadFenceData = downloadFenceData;
        return this;
    }
    public MapWriterBuilder withDownloadFenceGateData(Boolean downloadFenceGateData) {
        this.downloadFenceGateData = downloadFenceGateData;
        return this;
    }
    public MapWriterBuilder withDownloadRailData(Boolean downloadRailData) {
        this.downloadRailData = downloadRailData;
        return this;
    }
    public MapWriterBuilder withDownloadDoorData(Boolean downloadDoorData) {
        this.downloadDoorData = downloadDoorData;
        return this;
    }
    public MapWriterBuilder withBisectedData(Boolean downloadBisectedData) {
        this.downloadDoorData = downloadDoorData;
        return this;
    }
    public MapWriterBuilder withWaterloggedData(Boolean downloadWaterloggedData) {
        this.downloadWaterloggedData = downloadWaterloggedData;
        return this;
    }
    public MapWriterBuilder withPowerableData(Boolean downloadPoweredData) {
        this.downloadPoweredData = downloadPoweredData;
        return this;
    }
    public MapWriterBuilder withAgeableData(Boolean downloadAgeableData) {
        this.downloadAgeableData = downloadAgeableData;
        return this;
    }
    public MapWriterBuilder withOpenableData(Boolean downloadopenableData) {
        this.downloadOpenableData = downloadOpenableData;
        return this;
    }
    public MapWriterBuilder withAnaloguePowerableData(Boolean downloadAnaloguePowerableData) {
        this.downloadAnaloguePowerableData = downloadAnaloguePowerableData;
        return this;
    }
    public MapWriterBuilder withAttachableData(Boolean downloadAttachableData) {
        this.downloadAttachableData = downloadAttachableData;
        return this;
    }
    public MapWriterBuilder withBeehiveData(Boolean downloadBeehiveData) {
        this.downloadBeehiveData = downloadBeehiveData;
        return this;
    }
    public MapWriterBuilder withBellData(Boolean downloadBellData) {
        this.downloadBellData = downloadBellData;
        return this;
    }
    public MapWriterBuilder withBigDripleafData(Boolean downloadBigDripleafData) {
        this.downloadBigDripleafData = downloadBigDripleafData;
        return this;
    }
    public MapWriterBuilder withBubbleColumnData(Boolean downloadBubbleColumnData) {
        this.downloadBubbleColumnData = downloadBubbleColumnData;
        return this;
    }
    public MapWriterBuilder withCakeData(Boolean downloadCakeData) {
        this.downloadCakeData = downloadCakeData;
        return this;
    }
}
