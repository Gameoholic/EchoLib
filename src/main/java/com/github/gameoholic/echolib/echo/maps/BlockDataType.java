package com.github.gameoholic.echolib.echo.maps;

import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;

public enum BlockDataType {
    //To add conditional block data, make sure to add it here, in MapWriterBuilder.java and in ConditionalDataHandler.java
    INVALID(0xFFFF),
    BIOME(0),

    //The order in which the conditional block data types are declared are the order in which they will be read/written:
    STAIRS_DATA(2, Stairs.class), RAIL_DATA(9, Rail.class), WALL_DATA(4, Wall.class), DOOR_DATA(12, Door.class), FENCE_DATA(7, Fence.class),
    FENCE_GATE_DATA(8, Gate.class), SLAB_DATA(6, Slab.class), BED_DATA(3, Bed.class), BAMBOO_DATA(5, Bamboo.class), BEEHIVE_DATA(19, Beehive.class)
    , BELL_DATA(20, Bell.class), BIGDRIPLEAF_DATA(21, BigDripleaf.class), BUBBLE_COLUMN_DATA(22, BubbleColumn.class), CAKE_DATA(23, Cake.class),

    FACING(1, Directional.class), BISECTED(10, Bisected.class), WATERLOGGED(13, Waterlogged.class), POWERABLE(14, Powerable.class),
    AGEABLE(15, Ageable.class), OPENABLE(16, Openable.class), ANALOGUE_POWERABLE(17, AnaloguePowerable.class), ATTACHABLE(18, Attachable.class),

    ;


    private final int numericValue;
    private final Class<? extends BlockData> blockDataInterface;
    BlockDataType(int numericValue, Class<? extends BlockData> blockDataInterface) {
        this.numericValue = numericValue;
        this.blockDataInterface = blockDataInterface;
    }
    BlockDataType(int numericValue) {
        this.numericValue = numericValue;
        this.blockDataInterface = null;
    }
    public int getNumericValue() {
        return numericValue;
    }
    public Class<? extends BlockData> getBlockDataInterface() {
        return blockDataInterface;
    }
    public boolean isConditional() {
        if (this.equals(INVALID) || this.equals(BIOME))
            return false;
        return true;
    }
}
