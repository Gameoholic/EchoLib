package com.github.gameoholic.echolib.echo.maps;

import com.github.gameoholic.echolib.EchoLib;
import com.github.gameoholic.echolib.echo.CustomDataWriter;
import com.github.gameoholic.echolib.maps.MapWriter;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class MapWriterImpl implements MapWriter {

    //Parameters:
    private UUID id;
    private String name;
    private String description;
    private World world;
    private Vector cornerCoords; //Positive x-z corner (south-east)
    private Vector size;
    //Which optional/conditional block data to write:
    private HashMap<BlockDataType, Boolean> writeBlockDataArguments; //ALL optional/conditional arguments
    public HashMap<BlockDataType, Boolean> getWriteBlockDataArguments() {
        return writeBlockDataArguments;
    }

    //Private members:
    private File file;
    private CustomDataWriter dataWriter;
    private ConditionalDataHandler conditionalDataHandler;
    private final int version; //Map file version. Will update only when Map code is changed.
    public MapWriterImpl(String name, String description, World world, Vector cornerCoords, Vector size,
                         HashMap<BlockDataType, Boolean> writeBlockDataArguments) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.world = world;
        this.cornerCoords = cornerCoords;
        this.size = size;

        this.writeBlockDataArguments = writeBlockDataArguments;

        this.version = EchoLib.mapFileVersion;
    }


    /**
     * Hardcoded limitations, not following them will result in undefined behaviour:
     * Name and description are encoded in UTF-8.
     * Name and description length must be between 1-65536 bytes.
     */

    @Override
    public void download() {
        //Create the file
        file = new File(EchoLib.plugin.getDataFolder(), String.format("data/maps/%s.echomap", id));
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //-----FILE HEADERS-----:

        //Version header:
        add2ByteHeader(version);
        //Name size header:
        try {
            add2ByteHeader(name.getBytes("UTF-8").length);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        //Name header:
        writeStringToFile(name);
        //Description size header:
        try {
            add2ByteHeader(description.getBytes("UTF-8").length);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        //Description header:
        writeStringToFile(description);

        //cornerCoords header:
        writeIntegerToFile(cornerCoords.getBlockX());
        writeIntegerToFile(cornerCoords.getBlockY());
        writeIntegerToFile(cornerCoords.getBlockZ());

        //size header:
        writeIntegerToFile(size.getBlockX());
        writeIntegerToFile(size.getBlockY());
        writeIntegerToFile(size.getBlockZ());

        //Block data headers, describes how the data for each block is structured:
        //2 bytes - datatype ID

        for (BlockDataType blockDataArgument : writeBlockDataArguments.keySet()) {
            if (writeBlockDataArguments.get(blockDataArgument) == true)
                add2ByteHeader(blockDataArgument.getNumericValue());
        }


        //Indicate that the rest of the file is for the block data
        add2ByteHeader(BlockDataType.INVALID.getNumericValue());


        //-----DOWNLOAD BLOCKS-----:
        downloadBlocks();
    }


    private void downloadBlocks() {
        dataWriter = new CustomDataWriter(file);
        conditionalDataHandler = new ConditionalDataHandler(dataWriter, this);

        int airBlocksSkipped = 0;
        for (int x = cornerCoords.getBlockX(); x > cornerCoords.getBlockX() - size.getBlockX(); x--) {
            for (int z = cornerCoords.getBlockZ(); z > cornerCoords.getBlockZ() - size.getBlockZ(); z--) {
                for (int y = cornerCoords.getBlockY(); y < cornerCoords.getBlockY() + size.getBlockY(); y++) {

                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType().isAir()) {
                        airBlocksSkipped++;
                        continue;
                    }

                    if (airBlocksSkipped > 0) {
                        dataWriter.writeBits(1, 1); ///The first bit indicates whether it's a block, or air block count. 1 - air blocks
                        dataWriter.writeBits(airBlocksSkipped, 32); //TODO: Optimize this
                        airBlocksSkipped = 0;
                    }

                    writeBlockData(block);
                }
            }
        }

        if (airBlocksSkipped > 0) { //Fix for if last block is air the loop exits out without writing the data
            dataWriter.writeBits(1, 1);
            dataWriter.writeBits(airBlocksSkipped, 32); //TODO: Optimize this
        }

        dataWriter.writeByte(); //Write last byte, even if it's not full
        dataWriter.writeBytesToFile(); //Write all bytes to file
    }

    private void writeBlockData(Block block) {
        dataWriter.writeBits(0, 1); //The first bit indicates whether it's a block, or air block count. 0 - blocks

        //Type - Required (11 bits)
        Material type = block.getType();
        int typeInt = Arrays.stream(Material.values()).toList().indexOf(type);
        dataWriter.writeBits(typeInt, 11);

        //Optional arguments:
        writeBlockBiome(block);


        conditionalDataHandler.writeConditionalData(block);
    }

    private void writeBlockBiome(Block block) {
        if (!writeBlockDataArguments.containsKey(BlockDataType.BIOME)) return;
        Biome biome = block.getBiome();
        int biomeInt = Arrays.stream(Biome.values()).toList().indexOf(biome);
        dataWriter.writeBits(biomeInt, 7);
    }
    private void add2ByteHeader(int headerValue) {
        byte[] header = new byte[2]; //the header in bytes, represented by 2 bytes. Big endian
        header[1] = (byte) (headerValue & 0xFF); // least significant byte
        header[0] = (byte) ((headerValue >> 8) & 0xFF); // most significant byte

        writeBytesToFile(header); //header final value in bytes
    }
    private void writeIntegerToFile(int data) {
        try (FileOutputStream stream = new FileOutputStream(file, true);
             DataOutputStream dos = new DataOutputStream(stream)) {
            dos.writeInt(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void writeStringToFile(String data) {
        try (FileOutputStream stream = new FileOutputStream(file, true)) {
            stream.write(data.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void writeBytesToFile(byte[] data) {
        try (FileOutputStream stream = new FileOutputStream(file, true)) {
            stream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
