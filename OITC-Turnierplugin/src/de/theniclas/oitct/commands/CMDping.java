package de.theniclas.oitct.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.theniclas.oitct.utils.Chat;

public class CMDping implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!command.getName().equalsIgnoreCase("ping")) return true;
		if(!(sender instanceof Player)) {
			sender.sendMessage("Dieser Befehl kann nur von Spielern genutzt werden");
			return true;
		}

		Player p = (Player) sender;
		int ping = ((CraftPlayer) p).getHandle().ping;
		
		if(ping <= 40) {
			p.sendMessage(Chat.PREFIX + "§bDein Ping§7: §a" + ping + "ms");
			return true;
		}
		if(ping >= 80) {
			p.sendMessage(Chat.PREFIX + "§bDein Ping§7: §c" + ping + "ms");
			return true;
		}
		p.sendMessage(Chat.PREFIX + "§bDein Ping§7: §6" + ping + "ms");
		return true;
	}

}
