package com.github.gameoholic.echolib.listeners;

import com.github.gameoholic.echolib.EchoLib;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener implements Listener {

        EchoLib plugin;
        public MovementListener(EchoLib plugin) {
                this.plugin = plugin;
        }
        @EventHandler
        public void onPlayerMove(PlayerMoveEvent e){



        }
}
