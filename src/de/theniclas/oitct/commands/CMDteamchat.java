package de.theniclas.oitct.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.utils.Chat;

public class CMDteamchat implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!command.getName().equalsIgnoreCase("teamchat")) return true;
		if(!(sender instanceof Player)) {
			sender.sendMessage("Dieser Befehl kann nur von Spielern genutzt werden");
			return true;
		}
		Player p = (Player) sender;
		
		Team team = Team.getTeam(Team.getTeamName(p.getUniqueId().toString()));
		if(team == null) {
			p.sendMessage(Chat.PREFIX + "§cDu bist in keinem Team");
			return true;
		}
		
		if(args.length == 0) {
			p.sendMessage(Chat.PREFIX + "§e/teamchat <Nachricht>");
			return true;
		}
		
		for(String uuid : team.getOnlineMembers()) {
			Player member = Bukkit.getPlayer(UUID.fromString(uuid));
			String str = String.join(" ", args);
			member.sendMessage("§7[§cTeam§7] §6" + p.getName() + "§8: §7" + str);
		}
		
		return true;
	}

}
