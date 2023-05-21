package com.github.gameoholic.echolib.echo;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomDataWriter {
    private byte b = 0x0;
    private int bitsUsed = 0; //bit used in b, from left to right
    private File file;
    private List<Byte> bytes = new ArrayList<>();
    public CustomDataWriter(File file) {
        this.file = file;
    }

    /**
     Writes a specified number of bits from the given input integer to the output stream.

     @param input The integer to read bits from.
     @param amount The number of bits to write to the output stream.

     @Example
     // Write the binary representation of the numbers 5 and 1 (101, 1) to the output stream, without padding.
     writeBits(0b101, 3); // Will write 101 to the file.
     writeBits(1, 1); // Will write 1 to the file.
     Upon writing this data to the file, the file will look like this: 10110000
     */

    public void writeBits(int input, int amount) {
        int bitsRemain = amount;
        for (int i = amount; i >= 1; i--) {
            if (bitsUsed == 8) {
                writeByte();
                writeBits(input, bitsRemain);
                return;
            }
            byte bit = getXBitFromRight(input, i);
            int shiftLeft = 8 - bitsUsed - 1;
            bit = (byte) (bit << shiftLeft);
            b = (byte) (b | bit);
            bitsUsed++;
            bitsRemain--;
        }
        if (bitsUsed == 8)
            writeByte();
    }

    //Returns bit number X from the right. For example: for 0b1101 0111 and X=4 will return 0
    private byte getXBitFromRight(int input, int X) {
        return (byte) ((input >>> (X - 1)) & 0x01);
    }

    //Adds the byte to the byte list. Should be called per full byte written to, and once after the entire byte writing process is finished.
    public void writeByte() {
        if (bitsUsed == 0)
            return;
        //printByte();
        bytes.add(b);

        //Reset byte:
        b = 0;
        bitsUsed = 0;
    }


    //Writes the bytes in the byte list into the file
    public void writeBytesToFile() {
        try (FileOutputStream stream = new FileOutputStream(file, true)) {
            byte[] byteArray = new byte[bytes.size()];
            for (int i = 0; i < bytes.size(); i++) {
                byteArray[i] = bytes.get(i);
            }
            stream.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes.clear();
    }
}
