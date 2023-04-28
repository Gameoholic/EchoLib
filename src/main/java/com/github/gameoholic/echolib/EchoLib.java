package com.github.gameoholic.echolib;

import com.github.gameoholic.echolib.commands.TestCommand;
import com.github.gameoholic.echolib.listeners.MovementListener;
import com.github.gameoholic.echolib.listeners.PlayerDeathListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import static net.kyori.adventure.text.Component.text;

public final class EchoLib extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new MovementListener(this), this);

        getCommand("test").setExecutor(new TestCommand());

    }




}
