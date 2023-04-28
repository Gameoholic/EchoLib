package com.github.gameoholic.echolib.commands;

import com.github.gameoholic.echolib.Replay;
import com.github.gameoholic.echolib.ReplayBuilder;
import com.github.gameoholic.echolib.echo.ReplayManagerImpl;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("foo.bar")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return true;
            }
        }

        ReplayManagerImpl replayManager = new ReplayManagerImpl();

        Replay replay = new ReplayBuilder("foo")
            .withBar("bar")
            .build();


        return true;
    }
}
