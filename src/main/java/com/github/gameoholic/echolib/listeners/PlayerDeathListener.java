package com.github.gameoholic.echolib.listeners;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.github.gameoholic.echolib.EchoLib;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class PlayerDeathListener implements Listener {
    private EchoLib plugin;

    public PlayerDeathListener(EchoLib plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer(); //Bukkit Player
        CraftPlayer craftPlayer = (CraftPlayer) p; //CraftBukkit Player
        ServerPlayer serverPlayer = craftPlayer.getHandle(); //NMS Player

        ServerGamePacketListenerImpl listener = serverPlayer.connection;

        MinecraftServer server = serverPlayer.getServer(); //NMS Server
        ServerLevel level = serverPlayer.getLevel(); //NMS World

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "CheesierPasta");
        String signature = "lpVaYyB295mDKKYkQUzll7HqUr/qnvaa1VpJPcQGri/NIc9NPFe6RJcvUiPLjTdOtxpREeOsruqCbEfkKYa8hQwRHAwx0ho7Cg0RYwRG5k7ocdDzZKaab63+9GR4rhwniLF5uNFbO8Mo1aoyiT8xcWlYBbUx45SgQAdHKK9908VYDxOH/LhtQd6gcTPXQeMOvZ+V9RVs8t8PihJvFBNpn/vSL61hiO3weTM5T9odsN6GtUYwM3OAxx5tdBPU5IIHEjWn0gYE2SR2b3y2e8isTUX4Y7we36cLaBbV4/X+RuVOdSmn/i014QgRsfr+wdA4pmhJrz1B8rZc+b7ANwcgBxZkQxtaTiCGlkVaVFscVuvmlZ9jUszqqu7JLOv/lvT7qEfwBgnv5HWkd7A3qcqFdGI6vXsR69OAXHOpvaOIGhSowE2lAvg0zDn+8gW6N73An9cNInU0EL6mAZ1/u0INAX/Pabiwa4+kzR8oGU761xNM+Ajl1VXnMlld2pKYu85BEn5gNbDXvlsDcO/ly1T88vAbDgMuSPQrumsDfr/wOmkj9HW1L7KUsyQl43UEpEIqDaMbEmQuu8ZDpscOzyN2vxJac3W7N55su4HqObMP/OiPiM2mjmgXy6c7q7fFTc2DxsRPYvyhPzGxUeVBJz67stejhgs9lpnZDUDringdoqg=";
        String texture = "ewogICJ0aW1lc3RhbXAiIDogMTY4MjAwODM1Njg1NywKICAicHJvZmlsZUlkIiA6ICI0NWI4MDhkMmFkZjM0YjUxOWIzYmI5MjU2N2UwODg2ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDaGVlc2llclBhc3RhIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI0YmRiNTEwMjhhYzhkN2M0NTUwNWJmNDRkNjZkYzY5NTNjYzRhNjQzNTI5NzFkZjJkMGVhYmIwNTNiMTg2NjEiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iMGNjMDg4NDA3MDA0NDczMjJkOTUzYTAyYjk2NWYxZDY1YTEzYTYwM2JmNjRiMTdjODAzYzIxNDQ2ZmUxNjM1IgogICAgfQogIH0KfQ==";

        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));

        ServerPlayer npc = new ServerPlayer(server, level, gameProfile);


        ClientboundPlayerInfoUpdatePacket addPlayerPacket = new ClientboundPlayerInfoUpdatePacket(
                ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc);
        ClientboundPlayerInfoUpdatePacket updateListedPacket = new ClientboundPlayerInfoUpdatePacket(
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED, npc);

        npc.setPos(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());

        listener.send(addPlayerPacket);
        listener.send(updateListedPacket);


    }


}
