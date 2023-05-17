package com.github.gameoholic.echolib.commands;

import com.github.gameoholic.echolib.*;
import com.github.gameoholic.echolib.maps.MapWriter;
import com.github.gameoholic.echolib.maps.MapWriterBuilder;
import com.github.gameoholic.echolib.maps.ReplayMap;
import com.github.gameoholic.echolib.maps.ReplayMapBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Door;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("foo.bar")) {
                player.getLocation().getBlock().setType(Material.GRASS_BLOCK);
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return true;
            }
        }

        ReplayManager replayManager = EchoLib.replayManager;

        Replay replay = new ReplayBuilder("foo")
            .withBar("bar")
            .build();



        if (args[0].equals("download")) {

            Location playerLoc = ((Player)sender).getLocation();

            MapWriter mapDownloader = new MapWriterBuilder(
                "Map name",
                "This is the map's description. I like cheese, actually, nah.",
                Bukkit.getWorlds().get(0),
                new Vector(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ()),
                new Vector(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]))
            )
                .withDownloadBlockBiome(true)
                .build();
            mapDownloader.download();

        }
        else if (args[0].equals("a")) {
            ReplayMap mapReader = new ReplayMapBuilder(
                UUID.fromString(args[1])
            )
                .build();

            mapReader.load();
        }
        else {
            World world = Bukkit.getWorlds().get(0);
            Block block0 = world.getBlockAt(41, 83, 79);

            block0.setType(Material.DARK_OAK_DOOR);

            Bukkit.broadcastMessage("0: " + block0.getBlockData());
            if (block0.getBlockData() instanceof Door) {
                Bukkit.broadcastMessage("is door");
                Door bd = (Door) block0.getBlockData();
                bd.setOpen(true);
                block0.setBlockData(bd);
            }
            Bukkit.broadcastMessage("1: " + block0.getBlockData());
            if (block0.getBlockData() instanceof Directional) {
                Bukkit.broadcastMessage("is directional");
                Directional bd = (Directional) block0.getBlockData();
                bd.setFacing(BlockFace.WEST);
                block0.setBlockData(bd);
            }
            Bukkit.broadcastMessage("2: " + block0.getBlockData());


        }






        return true;
    }
}