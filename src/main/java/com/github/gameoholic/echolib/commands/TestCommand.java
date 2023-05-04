package com.github.gameoholic.echolib.commands;

import com.github.gameoholic.echolib.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

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


        Location playerLoc = ((Player)sender).getLocation();

        MapDownloader mapDownloader = new MapDownloaderBuilder(
            "Map name",
            "This is the map's description. I like cheese, actually, nah.",
            Bukkit.getWorlds().get(0),
            new Vector(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ()),
            new Vector(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])))
            .withDownloadBlockBiome(true)
            .build();
        mapDownloader.download();



        return true;
    }
}
