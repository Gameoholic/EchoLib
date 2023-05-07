package com.github.gameoholic.echolib.echo.maps;

import com.github.gameoholic.echolib.EchoLib;
import com.github.gameoholic.echolib.maps.MapWriter;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public class MapWriterImpl implements MapWriter {

    //Parameters:
    private UUID id;
    private String name;
    private String description;
    private World world;
    private Vector cornerCoords; //Positive x-z corner (south-east)
    private Vector size;
    private Boolean downloadBlockBiome;

    //Private members:
    private File file;
    private final int version; //Map file version. Will update only when Map code is changed.
    public MapWriterImpl(String name, String description, World world, Vector cornerCoords, Vector size, boolean downloadBlockBiome) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.world = world;
        this.cornerCoords = cornerCoords;
        this.size = size;
        this.downloadBlockBiome = downloadBlockBiome;

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
        //2 bytes - datatype ID, 2 bytes - length in bits

        //REQUIRED:
        add2ByteHeader(BlockDataTypeID.TYPE.getValue()); //datatype ID - 0 (type)
        add2ByteHeader(11); //data length - 11 bits

        if (downloadBlockBiome) {
            add2ByteHeader(BlockDataTypeID.BIOME.getValue()); //datatype ID - 1 (biome)
            add2ByteHeader(7); //data length - 7 bits
        }

        //Indicate that the rest of the file is for the block data
        add2ByteHeader(BlockDataTypeID.INVALID.getValue());


        //-----DOWNLOAD BLOCKS-----:
        downloadBlocks();
    }


    private void downloadBlocks() {
        CustomDataWriter dataWriter = new CustomDataWriter(file);

        int airBlocksSkipped = 0;
        for (int x = cornerCoords.getBlockX(); x > cornerCoords.getBlockX() - size.getBlockX(); x--) {
            for (int z = cornerCoords.getBlockZ(); z > cornerCoords.getBlockZ() - size.getBlockZ(); z--) {
                for (int y = cornerCoords.getBlockY(); y < cornerCoords.getBlockY() + size.getBlockY(); y++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType().isAir()) {
                        airBlocksSkipped++;
                        continue;
                    }

                    else if (airBlocksSkipped > 0) {
                        dataWriter.writeBits(1, 1); ///The first bit indicates whether it's a block, or air block count
                        dataWriter.writeBits(airBlocksSkipped, 32); //TODO: Optimize this
                        airBlocksSkipped = 0;
                    }

                    writeBlockData(block, dataWriter);
                }
            }
        }

        if (airBlocksSkipped > 0) { //If last block is air the loop exits out without writing the data
            dataWriter.writeBits(1, 1); ///The first bit indicates whether it's a block, or air block count
            dataWriter.writeBits(airBlocksSkipped, 32); //TODO: Optimize this
        }

        dataWriter.writeByte(); //Write last byte, even if it's not full
        dataWriter.writeBytesToFile(); //Write all bytes to file
    }

    private void writeBlockData(Block block, CustomDataWriter dataWriter) {
        /**
         * block data:
         * type: 11 bits
         * biome: 7 bits
         */

        dataWriter.writeBits(0, 1); //The first bit indicates whether it's a block, or air block count

        //Type: (11 bits)
        Material material = block.getType();
        int typeInt = Arrays.stream(Material.values()).toList().indexOf(material);
        dataWriter.writeBits(typeInt, 11);

        //Biome: (7 bits)
        if (downloadBlockBiome) {
            Biome biome = block.getBiome();
            int biomeInt = Arrays.stream(Biome.values()).toList().indexOf(biome);
            dataWriter.writeBits(biomeInt, 7);
        }
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
