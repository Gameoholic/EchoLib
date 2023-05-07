package com.github.gameoholic.echolib.echo.maps;

import com.github.gameoholic.echolib.EchoLib;
import com.github.gameoholic.echolib.maps.ReplayMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
    public ReplayMapImpl(UUID id) {
        this.id = id;

    }


    @Override
    public void read() {
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
        int version = readIntegerFromFile(2);
        if (version != EchoLib.mapFileVersion) {
            throw new RuntimeException("Map file is on version " + version + ", latest is " + EchoLib.mapFileVersion);
        }

        //Map name:
        int nameSize = readIntegerFromFile(2);

        byte[] nameBytes = new byte[nameSize];
        for (int i = 0; i < nameSize; i++) {
            nameBytes[i] = bytes[currentByte];
        }
        String name = readStringFromFile(nameBytes);

        //Map description:
        int descriptionSize = readIntegerFromFile(2);

        byte[] descriptionBytes = new byte[descriptionSize];
        for (int i = 0; i < descriptionSize; i++) {
            descriptionBytes[i] = bytes[currentByte];
        }
        String description = readStringFromFile(descriptionBytes);


        //Corner coords (X,Y,Z are integers):
        cornerCoordsX = readIntegerFromFile(4);
        cornerCoordsY = readIntegerFromFile(4);
        cornerCoordsZ = readIntegerFromFile(4);

        //Size (X,Y,Z are integers):
        sizeX = readIntegerFromFile(4);
        sizeY = readIntegerFromFile(4);
        sizeZ = readIntegerFromFile(4);

        //Block data headers, describes how the data for each block is structured:
        //2 bytes - datatype ID, 2 bytes - length in bits

        //0 - type
        //1 - biome
        //0xFF - marks end of block data structure headers

        int dataTypeID;
        int dataLength;
        do {
            dataTypeID = readIntegerFromFile(2);
            if (dataTypeID == BlockDataTypeID.INVALID.getValue())
                break;
            dataLength = readIntegerFromFile(2);

            //Type:
            if (dataTypeID == BlockDataTypeID.TYPE.getValue()) {
                System.out.println("type has " + dataLength + " bits");
            }
            //Biome:
            else if (dataTypeID == BlockDataTypeID.BIOME.getValue()) {
                System.out.println("biome has " + dataLength + " bits");
            }

        } while (dataTypeID != BlockDataTypeID.INVALID.getValue());



        currentX = 0;
        currentY = 0;
        currentZ = 0;
        //Read blocks:
        while (currentByte < bytes.length - 1) { //Will continue reading until the last byte was reached (even if notall the bits were read)
            readBlockData();

        }

    }

    private int currentX;
    private int currentY;
    private int currentZ;

    private void readBlockData() {
        Boolean isBlock = readBitsFromFile(1) == 0; //The first bit indicates whether it's a block, or air block count
        if (isBlock) {
            Material type = Material.values()[readBitsFromFile(11)];
            System.out.println("Block type " + type + " is of biome " + Biome.values()[readBitsFromFile(7)]);
            Bukkit.getWorlds().get(0).getBlockAt(cornerCoordsX - currentX, cornerCoordsY + currentY, cornerCoordsZ - currentZ).setType(type);
            incrementBlocks(1);
        }
        else {
            int airBlocks = readBitsFromFile(32);
            System.out.println("Blocks of air are " + airBlocks);
            incrementBlocks(airBlocks);
        }

//test

    }
    private void incrementBlocks(int x) {
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
                System.out.println("Finished reading.");
            }
        }
    }
    private int readBitsFromFile(int bitsAmount) {
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
    private int readIntegerFromFile(int bytesLength) {
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
