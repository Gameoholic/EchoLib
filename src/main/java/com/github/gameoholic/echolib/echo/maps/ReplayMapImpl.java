package com.github.gameoholic.echolib.echo.maps;

import com.github.gameoholic.echolib.EchoLib;
import com.github.gameoholic.echolib.maps.ReplayMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.UUID;

public class ReplayMapImpl implements ReplayMap {
    //Parameters:
    private UUID id;

    //Private members:
    //File reader variables:
    private File file;
    private byte[] bytes;
    private int currentByte;
    private int currentBit;
    //Map data variables:
    private int cornerCoordsX;
    private int cornerCoordsY;
    private int cornerCoordsZ;
    private int sizeX;
    private int sizeY;
    private int sizeZ;

    //Which conditional/optional block data to read:
    private HashMap<BlockDataType, Boolean> readBlockDataArguments;
    public HashMap<BlockDataType, Boolean> getReadBlockDataArguments() {
        return readBlockDataArguments;
    }

    //Current coordinates for block placement iteration:
    private int currentX;
    private int currentY;
    private int currentZ;
    public ReplayMapImpl(UUID id) {
        this.id = id;
    }


    @Override
    public void load() {
        ConditionalDataHandler.init(null, this, null);
        //Get the file
        file = new File(EchoLib.plugin.getDataFolder(), String.format("data/maps/%s.echomap", id));
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Read headers:
        currentByte = 0;

        //Version:
        int version = readBytesFromFile(2);
        if (version != EchoLib.mapFileVersion) {
            throw new RuntimeException("Map file is on version " + version + ", latest is " + EchoLib.mapFileVersion);
        }

        //Map name:
        int nameSize = readBytesFromFile(2);

        byte[] nameBytes = new byte[nameSize];
        for (int i = 0; i < nameSize; i++) {
            nameBytes[i] = bytes[currentByte];
        }
        String name = readStringFromFile(nameBytes);

        //Map description:
        int descriptionSize = readBytesFromFile(2);

        byte[] descriptionBytes = new byte[descriptionSize];
        for (int i = 0; i < descriptionSize; i++) {
            descriptionBytes[i] = bytes[currentByte];
        }
        String description = readStringFromFile(descriptionBytes);


        //Corner coords (X,Y,Z are integers):
        cornerCoordsX = readBytesFromFile(4);
        cornerCoordsY = readBytesFromFile(4);
        cornerCoordsZ = readBytesFromFile(4);

        //Size (X,Y,Z are integers):
        sizeX = readBytesFromFile(4);
        sizeY = readBytesFromFile(4);
        sizeZ = readBytesFromFile(4);

        //Block data headers, 2 bytes each - datatype ID:
        readBlockDataArguments = new HashMap<>();
        for (BlockDataType blockDataTypeID : BlockDataType.values()) {
            if (blockDataTypeID == BlockDataType.INVALID) continue;
            readBlockDataArguments.put(blockDataTypeID, false);
        }

        //If block data argument ID exists in file - add it to readBlockDataArguments
        int dataTypeID;
        do {
            dataTypeID = readBytesFromFile(2);
            if (dataTypeID == BlockDataType.INVALID.getNumericValue())
                break;
            else
                for (BlockDataType blockDataTypeID : BlockDataType.values()) {
                    if (blockDataTypeID.getNumericValue() == dataTypeID) {
                        readBlockDataArguments.put(blockDataTypeID, true);
                        break;
                    }
                }
        } while (dataTypeID != BlockDataType.INVALID.getNumericValue());


        currentX = 0;
        currentY = 0;
        currentZ = 0;

        //Read blocks:
        while (currentByte < bytes.length - 1) { //Will continue reading until the last byte was reached (even if notall the bits were read) Alteranatively, can check whether currentX >= sizeX
            readBlockData();
        }
        Bukkit.broadcastMessage("Finished");

    }


    private void readBlockData() {
        Boolean isBlock = readBitsFromFile(1) == 0; //The first bit indicates whether it's a block, or air block count. 0 - block

        if (!isBlock) {
            int airBlocks = readBitsFromFile(32);
            incrementBlocks(airBlocks);
            return;
        }

        Block block = Bukkit.getWorlds().get(0).getBlockAt(cornerCoordsX - currentX, cornerCoordsY + currentY, cornerCoordsZ - currentZ);



        Material type = Material.values()[readBitsFromFile(11)];
        Biome biome = null;

        //Optional block data:
        if (readBlockDataArguments.get(BlockDataType.BIOME))
            biome = Biome.values()[readBitsFromFile(7)];

        BlockData typeBlockData = type.createBlockData(); //the default block data for the type
        //Required block data:
        if (typeBlockData instanceof Bisected || typeBlockData instanceof Bed || typeBlockData instanceof Door) {
            block.setType(type, false);
        }
        else {
            block.setType(type);
        }

        //Optional block data:
        if (biome != null)
            block.setBiome(biome);

        //Conditional block data:
        ConditionalDataHandler.ReadConditionalData(block, type.createBlockData());

        incrementBlocks(1);
    }

    //Increments the amount of the blocks iterated by x amount of blocks
    private void incrementBlocks(int x) {
        //Block loading is like this:
        //For every X value, you go over every Z value, and for every Z value you go over every Y value
        for (int i = 0; i < x; i++) {
            currentY += 1;
            if (currentY >= sizeY) {
                currentY = 0;
                currentZ += 1;
            }
            if (currentZ >= sizeZ) {
                currentZ = 0;
                currentX += 1;
            }
            if (currentX >= sizeX) {
                Bukkit.broadcastMessage("Finished reading.");
            }
        }
    }
    int readBitsFromFile(int bitsAmount) {
        int result = 0;
        byte b = bytes[currentByte];

        int bitsProcessed = 0;
        int localByteIndex = 0; //The bit index in the local 8-bit segment of result
        while (bitsProcessed < bitsAmount) {
            if (7 - currentBit < 0) {
                currentBit = 0;
                currentByte += 1;
                b = bytes[currentByte];
                result <<= localByteIndex;
                localByteIndex = 0;
            }

            byte bitMask = (byte) (1 << (7 - currentBit));
            byte bit = (byte) (((b & bitMask) & 0xFF) >>> (7 - currentBit)); //the bit in position i
            result |= (bit << (7 - localByteIndex));

            currentBit++;
            localByteIndex++;
            bitsProcessed++;
        }

        result = result >>> (8 - localByteIndex);

        return result;
    }
    public int readBytesFromFile(int bytesLength) {
        int value = bytes[currentByte] & 0xFF; //Make sure if it's a negative value the most significant bit isn't extended
        for (int i = 1; i < bytesLength; i++) {
            value = value << 8;
            value |= bytes[currentByte + i] & 0xFF;
        }
        currentByte += bytesLength;
        return value;
    }
    private String readStringFromFile(byte[] b) {
        currentByte += b.length;
        return new String(b, StandardCharsets.UTF_8);
    }

    private void printByte(Byte b) {
        System.out.println(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
    }

}
