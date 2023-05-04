package com.github.gameoholic.echolib;

import com.github.gameoholic.echolib.commands.TestCommand;
import com.github.gameoholic.echolib.echo.ReplayManagerImpl;
import com.github.gameoholic.echolib.listeners.MovementListener;
import com.github.gameoholic.echolib.listeners.PlayerDeathListener;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;

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
