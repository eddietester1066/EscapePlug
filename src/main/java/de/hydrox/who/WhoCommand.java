package de.hydrox.who;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.hydrox.bukkit.DroxPerms.DroxPermsAPI;

public class WhoCommand implements CommandExecutor {

	private DroxPermsAPI perms = null;
	private PlayerComparator playerCompare = null;

	public WhoCommand(DroxPermsAPI perms) {
		this.perms = perms;
		this.playerCompare = new PlayerComparator(perms);
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (!(sender.hasPermission("escapeplug.who.list"))) {
			sender.sendMessage(ChatColor.RED
					+ "You don't have permission for /who.");
			return true;
		}
		if (args.length == 0) {
			Player[] players = Bukkit.getOnlinePlayers();
			Arrays.sort(players, playerCompare);
			sender.sendMessage(ChatColor.GOLD + "Players online: "
					+ players.length + "/" + Bukkit.getMaxPlayers() + ".");
			StringBuffer playersString = new StringBuffer();
			for (Player player : players) {
				if (perms != null) {
					String group = perms.getPlayerGroup(player.getName());
					String groupPrefix = perms.getGroupInfo(group, "prefix");
					String playerPrefix = perms.getPlayerInfo(player.getName(),
							"prefix");
					if (playerPrefix != null) {
						playerPrefix = playerPrefix.replace("&", "\247");
						playersString.append(playerPrefix);
					} else if (groupPrefix != null) {
						groupPrefix = groupPrefix.replace("&", "\247");
						playersString.append(groupPrefix);
					} else {
						playersString.append(ChatColor.WHITE);
					}
				}
				playersString.append(player.getName() + " ");
			}
			sender.sendMessage(ChatColor.GOLD + "Players: " + playersString);
		}
		if (args.length == 1) {
			if (!(sender.hasPermission("escapeplug.who.player"))) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for /who " + args[0] + ".");
				return true;
			}
			Player player = Bukkit.getPlayer(args[0]);
			if (player != null) {				
				sender.sendMessage(ChatColor.GOLD + "Player: " + player.getName());
				sender.sendMessage(ChatColor.GOLD + "IP: " + player.getAddress());
				Location location = player.getLocation();
				sender.sendMessage(ChatColor.GOLD + "World: " + location.getWorld().getName());
				sender.sendMessage(ChatColor.GOLD + "Coords: (" + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + ")");
				sender.sendMessage(ChatColor.GOLD + "Health: " + player.getHealth() + "/20");
				if (perms != null) {
					String group = perms.getPlayerGroup(player.getName());
					String groupPrefix = perms.getGroupInfo(group, "prefix");
					groupPrefix = groupPrefix.replace("&", "\247");
					sender.sendMessage(ChatColor.GOLD + "Group: " + groupPrefix + group);
					String playerPrefix = perms.getPlayerInfo(player.getName(), "prefix");
					String effectivePrefix = "";
					if (playerPrefix != null) {
						effectivePrefix = playerPrefix.replace("&", "\247");
					} else if (groupPrefix!= null) {
						effectivePrefix = groupPrefix;
					}
					sender.sendMessage(ChatColor.GOLD + "Shown Name: " + effectivePrefix + player.getName());
				}
				sender.sendMessage(ChatColor.GOLD + "OP: " + player.isOp());
			}
		}
		return true;
	}

}
