package com.github.gameoholic.echolib.echo.maps;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;
import java.util.LinkedHashMap;

import java.util.Arrays;

public class ConditionalDataHandler {
    private static CustomDataWriter dataWriter;
    private static ReplayMapImpl mapReader;
    private static MapWriterImpl mapWriter;
    private static LinkedHashMap<Class<? extends BlockData>, BlockDataType> conditionalBlockDataMappings = new LinkedHashMap<>() {{
        for (BlockDataType blockDataType : BlockDataType.values()) {
            if (blockDataType.isConditional())
                put(blockDataType.getBlockDataInterface(), blockDataType);
        }
    }}; //Maps block data interfaces to their respective block data type enum
    public static void init(CustomDataWriter aDataWriter, ReplayMapImpl aMapReader, MapWriterImpl aMapWriter) {
        if (aDataWriter != null)
            dataWriter = aDataWriter;
        if (aMapReader != null)
            mapReader = aMapReader;
        if (aMapWriter != null)
            mapWriter = aMapWriter;
    }
    public static void writeConditionalData(Block block) {
        for (Class<? extends BlockData> blockDataInterface : conditionalBlockDataMappings.keySet())
            WriteData(blockDataInterface, block);
    }
    public static void ReadConditionalData(Block block, BlockData defaultBlockData) {
        for (Class<? extends BlockData> blockDataInterface : conditionalBlockDataMappings.keySet())
            ReadData(blockDataInterface, block, defaultBlockData);
    }
    private static void WriteData(Class<? extends BlockData> blockDataInterface, Block block) {
        if (!mapWriter.getWriteBlockDataArguments().get(conditionalBlockDataMappings.get(blockDataInterface)))
            return;
        BlockData blockData = block.getBlockData();

        if (!blockDataInterface.isAssignableFrom(blockData.getClass())) return;


        if (blockDataInterface.equals(Stairs.class))
            WriteStairsData((Stairs) blockData);
        else if (blockDataInterface.equals(Rail.class))
            WriteRailData((Rail) blockData);
        else if (blockDataInterface.equals(Wall.class))
            WriteWallData((Wall) blockData);
        else if (blockDataInterface.equals(Door.class))
            WriteDoorData((Door) blockData);
        else if (blockDataInterface.equals(Fence.class))
            WriteFenceData((Fence) blockData);
        else if (blockDataInterface.equals(Gate.class))
            WriteFenceGateData((Gate) blockData);
        else if (blockDataInterface.equals(Slab.class))
            WriteSlabData((Slab) blockData);
        else if (blockDataInterface.equals(Bed.class))
            WriteBedData((Bed) blockData);
        else if (blockDataInterface.equals(Bamboo.class))
            WriteBambooData((Bamboo) blockData);
        else if (blockDataInterface.equals(Beehive.class))
            WriteBeehiveData((Beehive) blockData);
        else if (blockDataInterface.equals(Bell.class))
            WriteBellData((Bell) blockData);
        else if (blockDataInterface.equals(BigDripleaf.class))
            WriteBigDripleafData((BigDripleaf) blockData);
        else if (blockDataInterface.equals(BubbleColumn.class))
            WriteBubbleColumnData((BubbleColumn) blockData);
        else if (blockDataInterface.equals(Cake.class))
            WriteCakeData((Cake) blockData);

        if (blockDataInterface.equals(Directional.class))
            WriteFaceData((Directional) blockData);
        if (blockDataInterface.equals(Bisected.class))
            WriteBisectedData((Bisected) blockData);
        if (blockDataInterface.equals(Waterlogged.class))
            WriteWaterloggedData((Waterlogged) blockData);
        if (blockDataInterface.equals(Powerable.class))
            WritePowerableData((Powerable) blockData);
        if (blockDataInterface.equals(Ageable.class))
            WriteAgeableData((Ageable) blockData);
        if (blockDataInterface.equals(Openable.class))
            WriteOpenableData((Openable) blockData);
        if (blockDataInterface.equals(AnaloguePowerable.class))
            WriteAnaloguePowerableData((AnaloguePowerable) blockData);
        if (blockDataInterface.equals(Attachable.class))
            WriteAttachableData((Attachable) blockData);
    }

    private static void ReadData(Class<? extends BlockData> blockDataInterface, Block block, BlockData defaultBlockData) {
        if (!mapReader.getReadBlockDataArguments().get(conditionalBlockDataMappings.get(blockDataInterface)))
            return;
        BlockData blockData = defaultBlockData;
        if (!blockDataInterface.isAssignableFrom(blockData.getClass())) return;


        if (blockDataInterface.equals(Stairs.class))
            ReadStairsData(block, (Stairs) blockData);
        else if (blockDataInterface.equals(Rail.class))
            ReadRailData(block, (Rail) blockData);
        else if (blockDataInterface.equals(Wall.class))
            ReadWallData(block, (Wall) blockData);
        else if (blockDataInterface.equals(Door.class))
            ReadDoorData(block, (Door) blockData);
        else if (blockDataInterface.equals(Fence.class))
            ReadFenceData(block, (Fence) blockData);
        else if (blockDataInterface.equals(Gate.class))
            ReadFenceGateData(block, (Gate) blockData);
        else if (blockDataInterface.equals(Slab.class))
            ReadSlabData(block, (Slab) blockData);
        else if (blockDataInterface.equals(Bed.class))
            ReadBedData(block, (Bed) blockData);
        else if (blockDataInterface.equals(Bamboo.class))
            ReadBambooData(block, (Bamboo) blockData);
        else if (blockDataInterface.equals(Beehive.class))
            ReadBeehiveData(block, (Beehive) blockData);
        else if (blockDataInterface.equals(Bell.class))
            ReadBellData(block, (Bell) blockData);
        else if (blockDataInterface.equals(BigDripleaf.class))
            ReadBigDripleafData(block, (BigDripleaf) blockData);
        else if (blockDataInterface.equals(BubbleColumn.class))
            ReadBubbleColumnData(block, (BubbleColumn) blockData);
        else if (blockDataInterface.equals(Cake.class))
            ReadCakeData(block, (Cake) blockData);

        if (blockDataInterface.equals(Directional.class))
            ReadFaceData(block, (Directional) blockData);
        if (blockDataInterface.equals(Bisected.class))
            ReadBisectedData(block, (Bisected) blockData);
        if (blockDataInterface.equals(Waterlogged.class))
            ReadWaterloggedData(block, (Waterlogged) blockData);
        if (blockDataInterface.equals(Powerable.class))
            ReadPowerableData(block, (Powerable) blockData);
        if (blockDataInterface.equals(Ageable.class))
            ReadAgeableData(block, (Ageable) blockData);
        if (blockDataInterface.equals(Openable.class))
            ReadOpenableData(block, (Openable) blockData);
        if (blockDataInterface.equals(AnaloguePowerable.class))
            ReadAnaloguePowerableData(block, (AnaloguePowerable) blockData);
        if (blockDataInterface.equals(Attachable.class))
            ReadAttachableData(block, (Attachable) blockData);


    }

    private static void WriteCaveVinesPlantData(CaveVinesPlant caveVinesPlant) {
        //Get data:
        boolean isBerries = caveVinesPlant.isBerries();
        int isBerriesInt = isBerries ? 1 : 0;

        //Write data:
        dataWriter.writeBits(isBerriesInt, 1);
    }
    private static void ReadCaveVinesPlantData(Block block, CaveVinesPlant caveVinesPlant) {
        //Read data:
        boolean isBerries = mapReader.readBitsFromFile(1) == 1;

        //Set data:
        caveVinesPlant.setBerries(isBerries);

        block.setBlockData(caveVinesPlant);
    }
    private static void WriteCandleData(Candle candle) {
        //Get data:
        int candlesInt = candle.getCandles() - 1; //Values are 1-4, we make them 0-3

        //Write data:
        dataWriter.writeBits(candlesInt, 2);
    }
    private static void ReadCandleData(Block block, Candle candle) {
        //Read data:
        int candles = mapReader.readBitsFromFile(2) + 1;

        //Set data:
        candle.setCandles(candles);

        block.setBlockData(candle);
    }
    private static void WriteCampfireData(Campfire campfire) {
        //Get data:
        boolean signalFire = campfire.isSignalFire();
        int signalFireInt = signalFire ? 1 : 0;

        //Write data:
        dataWriter.writeBits(signalFireInt, 1);
    }
    private static void ReadCampfireData(Block block, Campfire campfire) {
        //Read data:
        Boolean signalFire = mapReader.readBitsFromFile(1) == 1;

        //Set data:
        campfire.setSignalFire(signalFire);

        block.setBlockData(campfire);
    }
    private static void WriteCakeData(Cake cake) {
        //Get data:
        int bitesInt = cake.getBites();

        //Write data:
        dataWriter.writeBits(bitesInt, 3); //0-6
    }
    private static void ReadCakeData(Block block, Cake cake) {
        //Read data:
        int bites = mapReader.readBitsFromFile(3);

        //Set data:
        cake.setBites(bites);

        block.setBlockData(cake);
    }

    private static void WriteBubbleColumnData(BubbleColumn bubbleColumn) {
        //Get data:
        boolean drag = bubbleColumn.isDrag();
        int dragInt = drag ? 1 : 0;

        //Write data:
        dataWriter.writeBits(dragInt, 1);
    }
    private static void ReadBubbleColumnData(Block block, BubbleColumn bubbleColumn) {
        //Read data:
        Boolean drag = mapReader.readBitsFromFile(1) == 1;

        //Set data:
        bubbleColumn.setDrag(drag);

        block.setBlockData(bubbleColumn);
    }

    private static void WriteBigDripleafData(BigDripleaf bigDripleaf) {
        //Get data:
        BigDripleaf.Tilt tilt = bigDripleaf.getTilt();
        int tiltInt = Arrays.stream(BigDripleaf.Tilt.values()).toList().indexOf(tilt);

        //Write data:
        dataWriter.writeBits(tiltInt, 2);
    }
    private static void ReadBigDripleafData(Block block, BigDripleaf bigDripleaf) {
        //Read data:
        BigDripleaf.Tilt tilt = BigDripleaf.Tilt.values()[mapReader.readBitsFromFile(2)];

        //Set data:
        bigDripleaf.setTilt(tilt);

        block.setBlockData(bigDripleaf);
    }
    private static void WriteBellData(Bell bell) {
        //Get data:
        Bell.Attachment attachment = bell.getAttachment();
        int attachmentInt = Arrays.stream(Bell.Attachment.values()).toList().indexOf(attachment);

        //Write data:
        dataWriter.writeBits(attachmentInt, 2);
    }
    private static void ReadBellData(Block block, Bell bell) {
        //Read data:
        Bell.Attachment attachment = Bell.Attachment.values()[mapReader.readBitsFromFile(2)];

        //Set data:
        bell.setAttachment(attachment);

        block.setBlockData(bell);
    }

    private static void WriteBeehiveData(Beehive beehive) {
        //Get data:
        int honeyLevelInt = beehive.getHoneyLevel();

        //Write data:
        dataWriter.writeBits(honeyLevelInt, 3); //0-5
    }
    private static void ReadBeehiveData(Block block, Beehive beehive) {
        //Read data:
        int honeyLevel = mapReader.readBitsFromFile(3);

        //Set data:
        beehive.setHoneyLevel(honeyLevel);

        block.setBlockData(beehive);
    }
    private static void WriteAttachableData(Attachable attachable) {
        //Get data:
        int isAttachedInt = attachable.isAttached() ? 1 : 0;

        //Write data:
        dataWriter.writeBits(isAttachedInt, 1);
    }
    private static void ReadAttachableData(Block block, Attachable attachable) {
        //Read data:
        Boolean attached = mapReader.readBitsFromFile(1) == 1;

        //Set data:
        attachable.setAttached(attached);

        block.setBlockData(attachable);
    }
    private static void WriteAnaloguePowerableData(AnaloguePowerable analoguePowerable) {
        //Get data:
        int powerInt = analoguePowerable.getPower();

        //Write data:
        dataWriter.writeBits(powerInt, 4); //0-15
    }
    private static void ReadAnaloguePowerableData(Block block, AnaloguePowerable analoguePowerable) {
        //Read data:
        int power = mapReader.readBitsFromFile(4);

        //Set data:
        analoguePowerable.setPower(power);

        block.setBlockData(analoguePowerable);
    }
    private static void WriteAgeableData(Ageable ageable) { //TODO: this might not work
        //Get data:
        int ageInt = ageable.getAge();

        //Write data:
        if (ageable instanceof Bamboo) //age between: 0-1 (2 values)
            dataWriter.writeBits(ageInt, 1);
        else if (ageable instanceof MangrovePropagule) //age between: 0-3 (4 values)
            dataWriter.writeBits(ageInt, 2);
        else if (ageable instanceof Fire) //age between: 0-15 (16 values)
            dataWriter.writeBits(ageInt, 4);
        else if (ageable instanceof Cocoa) //age between: 0-2 (3 values)
            dataWriter.writeBits(ageInt, 2);
        else if (ageable instanceof CaveVines) //age between: 0-25 (26 values)
            dataWriter.writeBits(ageInt, 5);
    }
    private static void ReadAgeableData(Block block, Ageable ageable) { //TODO: this might not work
        //Read data:
        int age;
        if (ageable instanceof Bamboo) //age between: 0-1 (2 values)
            age = mapReader.readBitsFromFile(1);
        else if (ageable instanceof MangrovePropagule) //age between: 0-3 (4 values)
            age = mapReader.readBitsFromFile(2);
        else if (ageable instanceof Fire) //age between: 0-15 (16 values)
            age = mapReader.readBitsFromFile(4);
        else if (ageable instanceof Cocoa) //age between: 0-2 (3 values)
            age = mapReader.readBitsFromFile(2);
        else if (ageable instanceof CaveVines) //age between: 0-25 (26 values)
            age = mapReader.readBitsFromFile(5);
        else
            throw new RuntimeException("Invalid ageable");

        //Set data:
        ageable.setAge(age);

        block.setBlockData(ageable);
    }
    private static void WritePowerableData(Powerable powerable) {
        //Get data:
        int isPoweredInt = powerable.isPowered() ? 1 : 0;

        //Write data:
        dataWriter.writeBits(isPoweredInt, 1);
    }
    private static void ReadPowerableData(Block block, Powerable powerable) {
        //Read data:
        Boolean isPowered = mapReader.readBitsFromFile(1) == 1;

        //Set data:
        powerable.setPowered(isPowered);

        block.setBlockData(powerable);
    }
    private static void WriteWaterloggedData(Waterlogged waterlogged) {
        //Get data:
        int isWaterloggedInt = waterlogged.isWaterlogged() ? 1 : 0;

        //Write data:
        dataWriter.writeBits(isWaterloggedInt, 1);
    }
    private static void ReadWaterloggedData(Block block, Waterlogged waterlogged) {
        //Read data:
        Boolean isWaterlogged = mapReader.readBitsFromFile(1) == 1;

        //Set data:
        waterlogged.setWaterlogged(isWaterlogged);

        block.setBlockData(waterlogged);
    }
    private static void WriteBisectedData(Bisected bisected) {
        //Get data:
        Bisected.Half half = bisected.getHalf();
        int halfInt = Arrays.stream(Bisected.Half.values()).toList().indexOf(half);

        //Write data:
        dataWriter.writeBits(halfInt, 1);
    }
    private static void ReadBisectedData(Block block, Bisected bisected) {
        //Read data:
        Bisected.Half half = Bisected.Half.values()[mapReader.readBitsFromFile(1)];

        //Set data:
        bisected.setHalf(half);

        block.setBlockData(bisected);
    }
    private static void WriteDoorData(Door door) {
        //Get data:
        Door.Hinge doorHinge = door.getHinge();
        int doorHingeInt = Arrays.stream(Door.Hinge.values()).toList().indexOf(doorHinge);

        //Write data:
        dataWriter.writeBits(doorHingeInt, 1); //2 values, 2^1 = 2
    }
    private static void ReadDoorData(Block block, Door door) {
        //Read data:
        Door.Hinge hinge = Door.Hinge.values()[mapReader.readBitsFromFile(1)];

        //Set data:
        door.setHinge(hinge);

        block.setBlockData(door);
    }
    private static void WriteFenceGateData(Gate fenceGate) {
        //Get data:
        int fenceInWallInt = fenceGate.isInWall() ? 1 : 0;

        //Write data:
        dataWriter.writeBits(fenceInWallInt, 1);
    }
    private static void ReadFenceGateData(Block block, Gate fenceGate) {
        //Read data:
        boolean inWall = mapReader.readBitsFromFile(1) == 1;

        //Set data:
        fenceGate.setInWall(inWall);

        block.setBlockData(fenceGate);
    }
    private static void WriteFenceData(Fence fence) {
        //Get data:
        int fenceEastInt = fence.getFaces().contains(BlockFace.EAST) ? 1 : 0;
        int fenceWestInt = fence.getFaces().contains(BlockFace.WEST) ? 1 : 0;
        int fenceSouthInt = fence.getFaces().contains(BlockFace.SOUTH) ? 1 : 0;
        int fenceNorthInt = fence.getFaces().contains(BlockFace.NORTH) ? 1 : 0;

        //Write data:
        dataWriter.writeBits(fenceEastInt, 1);
        dataWriter.writeBits(fenceWestInt, 1);
        dataWriter.writeBits(fenceSouthInt, 1);
        dataWriter.writeBits(fenceNorthInt, 1);
    }
    private static void ReadFenceData(Block block, Fence fence) {
        //Read data:
        boolean fenceEast = mapReader.readBitsFromFile(1) == 1;
        boolean fenceWest = mapReader.readBitsFromFile(1) == 1;
        boolean fenceSouth = mapReader.readBitsFromFile(1) == 1;
        boolean fenceNorth = mapReader.readBitsFromFile(1) == 1;

        //Set data:
        fence.setFace(BlockFace.EAST, fenceEast);
        fence.setFace(BlockFace.WEST, fenceWest);
        fence.setFace(BlockFace.SOUTH, fenceSouth);
        fence.setFace(BlockFace.NORTH, fenceNorth);

        block.setBlockData(fence);
    }
    private static void WriteSlabData(Slab slab) {
        //Get data:
        Slab.Type slabType = slab.getType();

        //Convert data:
        int slabTypeInt = Arrays.stream(Slab.Type.values()).toList().indexOf(slabType);

        //Write data:
        dataWriter.writeBits(slabTypeInt, 2); //3 values, 2^2 = 4 bits
    }
    private static void ReadSlabData(Block block, Slab slab) {
        //Read data:
        Slab.Type slabType = Slab.Type.values()[mapReader.readBitsFromFile(2)];

        //Set data:
        slab.setType(slabType);

        block.setBlockData(slab);
    }
    private static void WriteBambooData(Bamboo bamboo) {
        //Get data:
        Bamboo.Leaves leaves = bamboo.getLeaves();
        int leavesInt = Arrays.stream(Bamboo.Leaves.values()).toList().indexOf(leaves);
        int stageInt = bamboo.getStage(); // 0/1

        //Write data:
        dataWriter.writeBits(leavesInt, 2); //3 values, 2^2 = 4 bits
        dataWriter.writeBits(stageInt, 1); //2 values: 0/1
    }
    private static void ReadBambooData(Block block, Bamboo bamboo) {
        //Read data:
        Bamboo.Leaves leaves = Bamboo.Leaves.values()[mapReader.readBitsFromFile(2)];
        int stage = mapReader.readBitsFromFile(1);

        //Set data:
        bamboo.setLeaves(leaves);
        bamboo.setStage(stage);

        block.setBlockData(bamboo);
    }
    private static void WriteBedData(Bed bed) {
        //Get data:
        Bed.Part bedPart = bed.getPart();

        //Convert data:
        int bedPartInt = Arrays.stream(Bed.Part.values()).toList().indexOf(bedPart);

        //Write data:
        dataWriter.writeBits(bedPartInt, 1);
    }
    private static void ReadBedData(Block block, Bed bed) {
        //Read data:
        Bed.Part bedPart = Bed.Part.values()[mapReader.readBitsFromFile(1)];

        //Set data:
        bed.setPart(bedPart);

        block.setBlockData(bed);
    }
    private static void WriteRailData(Rail rail) {
        //Get data:
        Rail.Shape railShape = rail.getShape();
        int railShapeInt = Arrays.stream(Rail.Shape.values()).toList().indexOf(railShape);

        //Write data:
        dataWriter.writeBits(railShapeInt, 4);
    }
    private static void ReadRailData(Block block, Rail rail) {
        //Read data:
        Rail.Shape railShape = Rail.Shape.values()[mapReader.readBitsFromFile(4)];

        //Set data:
        rail.setShape(railShape);

        block.setBlockData(rail);
    }
    private static void WriteStairsData(Stairs stairs) {
        //Get data:
        Stairs.Shape stairsShape = stairs.getShape();
        int stairsShapeInt = Arrays.stream(Stairs.Shape.values()).toList().indexOf(stairsShape);

        //Write data:
        dataWriter.writeBits(stairsShapeInt, 3);
    }
    private static void ReadStairsData(Block block, Stairs stairs) {
        //Read data:
        Stairs.Shape stairsShape = Stairs.Shape.values()[mapReader.readBitsFromFile(3)];

        //Set data:
        stairs.setShape(stairsShape);

        block.setBlockData(stairs);
    }
    private static void WriteFaceData(Directional directionalBlock) {
        //Get data:
        BlockFace blockFace = directionalBlock.getFacing();

        //Convert data:
        int blockFaceInt = Arrays.stream(BlockFace.values()).toList().indexOf(blockFace);

        //Write data:
        dataWriter.writeBits(blockFaceInt, 5);
    }
    private static void ReadFaceData(Block block, Directional directionalBlock) {
        //Read data:
        BlockFace blockFace = BlockFace.values()[mapReader.readBitsFromFile(5)];

        //Set data:
        directionalBlock.setFacing(blockFace);

        block.setBlockData(directionalBlock);
    }
    private static void WriteWallData(Wall wall) {
        //Get data:
        Boolean wallIsUp = wall.isUp();
        Wall.Height wallHeightEast = wall.getHeight(BlockFace.EAST);
        Wall.Height wallHeightWest = wall.getHeight(BlockFace.WEST);
        Wall.Height wallHeightNorth = wall.getHeight(BlockFace.NORTH);
        Wall.Height wallHeightSouth = wall.getHeight(BlockFace.SOUTH);

        //Convert data:
        int wallIsUpInt = wallIsUp ? 1 : 0;
        int wallHeightEastInt = Arrays.stream(Wall.Height.values()).toList().indexOf(wallHeightEast);
        int wallHeightWestInt = Arrays.stream(Wall.Height.values()).toList().indexOf(wallHeightWest);
        int wallHeightNorthInt = Arrays.stream(Wall.Height.values()).toList().indexOf(wallHeightNorth);
        int wallHeightSouthInt = Arrays.stream(Wall.Height.values()).toList().indexOf(wallHeightSouth);

        //Write data:
        dataWriter.writeBits(wallIsUpInt, 1);
        dataWriter.writeBits(wallHeightEastInt, 2);
        dataWriter.writeBits(wallHeightWestInt, 2);
        dataWriter.writeBits(wallHeightNorthInt, 2);
        dataWriter.writeBits(wallHeightSouthInt, 2);
    }
    private static void ReadWallData(Block block, Wall wall) {
        //Read data:
        Boolean wallIsUp = mapReader.readBitsFromFile(1) == 1;
        Wall.Height wallHeightEast = Wall.Height.values()[mapReader.readBitsFromFile(2)];
        Wall.Height wallHeightWest = Wall.Height.values()[mapReader.readBitsFromFile(2)];
        Wall.Height wallHeightNorth = Wall.Height.values()[mapReader.readBitsFromFile(2)];
        Wall.Height wallHeightSouth = Wall.Height.values()[mapReader.readBitsFromFile(2)];

        //Set data:
        wall.setUp(wallIsUp);
        wall.setHeight(BlockFace.EAST, wallHeightEast);
        wall.setHeight(BlockFace.WEST, wallHeightWest);
        wall.setHeight(BlockFace.NORTH, wallHeightNorth);
        wall.setHeight(BlockFace.SOUTH, wallHeightSouth);

        block.setBlockData(wall);
    }

    private static void WriteOpenableData(Openable openable) {
        //Get data:
        Boolean isOpen = openable.isOpen();
        int isOpenInt = isOpen ? 1 : 0;

        //Write data:
        dataWriter.writeBits(isOpenInt, 1);
    }
    private static void ReadOpenableData(Block block, Openable openable) {
        //Read data:
        Boolean isOpen = mapReader.readBitsFromFile(1) == 1;

        //Set data:
        openable.setOpen(isOpen);

        block.setBlockData(openable);
    }
}
