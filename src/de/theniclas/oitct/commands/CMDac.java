package de.theniclas.oitct.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.SnicoTV.ClickAndRange.ClickAndRange;
import de.theniclas.oitct.utils.Chat;

public class CMDac implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!command.getName().equalsIgnoreCase("ac")) return true;
		if(!(sender instanceof Player)) {
			sender.sendMessage("Dieser Befehl kann nur von Spielern genutzt werden");
			return true;
		}
		Player p = (Player) sender;
		if(!p.hasPermission("oitct.ac")) {
			p.sendMessage(Chat.NO_PERM);
			return true;
		}
		if(Bukkit.getPluginManager().getPlugin("ClickAndRange") == null) {
			sender.sendMessage(Chat.PREFIX + "§cDas Plugin ist nicht installiert");
			return true;
		}
		
		ClickAndRange.getPlugin(ClickAndRange.class).openCarInv(p);
		
		return true;
	}

}
