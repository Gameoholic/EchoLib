package com.github.gameoholic.echolib;

import com.github.gameoholic.echolib.commands.TestCommand;
import com.github.gameoholic.echolib.listeners.MovementListener;
import com.github.gameoholic.echolib.listeners.PlayerDeathListener;
import com.github.gameoholic.echolib.maps.ReplayMap;
import com.github.gameoholic.echolib.maps.ReplayMapBuilder;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;

public final class EchoLib extends JavaPlugin implements Listener {
    //Library accessible fields:
    public static ReplayManager replayManager;
    //---

    //Read-only fields:
    public static final int replayFileVersion = 1;
    public static final int mapFileVersion = 1;

    public static EchoLib plugin;
    //---
    @Override
    public void onEnable() {
        plugin = this; //TEMP.
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new MovementListener(this), this);

        getCommand("test").setExecutor(new TestCommand());


        saveDefaultConfig();

        //Create data folders:
        File pluginDataFolder = getDataFolder();
        pluginDataFolder.mkdirs();
        new File(pluginDataFolder, "data").mkdirs();
        new File(pluginDataFolder, "data/maps").mkdirs();


        //MapDownloader mapDownloader = new MapDownloaderBuilder("Map name", "This is the map's description. I like cheese, actually, nah.", Bukkit.getWorlds().get(0), new Vector(0, 200, 0), new Vector(5, 5, 5)).build();
        //mapDownloader.download();



    }



}
