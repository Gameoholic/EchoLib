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

import static net.kyori.adventure.text.Component.text;

public final class EchoLib extends JavaPlugin implements Listener {

    //Library accessible fields:
    private static ReplayManagerImpl replayManager;
    //---

    //Read-only fields:
    public static final int replayAPIVersion = 1;
    //---
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new MovementListener(this), this);

        getCommand("test").setExecutor(new TestCommand());

    }


    private void injectPlayer(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                //Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "PACKET READ: " + ChatColor.RED + packet.toString());
                super.channelRead(channelHandlerContext, packet);
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
                //Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "PACKET WRITE: " + ChatColor.GREEN + packet.toString());


                if(packet instanceof PacketO){
                    PacketPlayOutBed packetPlayOutBed = (PacketPlayOutBed) packet;
                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "PACKET BLOCKED: " + ChatColor.GREEN + packetPlayOutBed.toString());
                    return;
                }
                super.write(channelHandlerContext, packet, channelPromise);
            }


        };

        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);

    }


}
