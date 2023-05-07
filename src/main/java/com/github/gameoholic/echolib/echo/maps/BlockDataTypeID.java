package com.github.gameoholic.echolib.echo.maps;

public enum BlockDataTypeID {
    TYPE(0), BIOME(1), INVALID(0xFFFF);
    private final int value;
    BlockDataTypeID(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
