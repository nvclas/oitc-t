package de.theniclas.oitct.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.theniclas.oitct.objects.Lobby;
import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.utils.Chat;

public class CMDlobby implements CommandExecutor {

	private void printHelp(Player p) {
		p.sendMessage(Chat.PREFIX + "§8--- §7/§blobby §8---");
		p.sendMessage(Chat.PREFIX + "§e/lobby setspawn");
		p.sendMessage(Chat.PREFIX + "§e/lobby setinv");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!command.getName().equalsIgnoreCase("lobby")) return true;
		if(!(sender instanceof Player)) {
			sender.sendMessage("Dieser Befehl kann nur von Spielern genutzt werden");
			return true;
		}
		Player p = (Player) sender;	
		
		if(!p.hasPermission("oitct.setup") || args.length == 0) {
			
			Team team = Team.getTeam(Team.getTeamName(p.getUniqueId().toString()));
			if(team != null && team.getFight() != null && team.getFight().getAlive().contains(p)) {
				p.sendMessage(Chat.PREFIX + "§cDu bist mitten im Kampf");
				return true;
			}
			Lobby.sendToLobby(p);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setspawn")) {
			Lobby.setSpawn(p.getLocation());
			p.sendMessage(Chat.PREFIX + "§bDer Lobbyspawn wurde gesetzt");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setinv")) {
			Lobby.setInventory(p.getInventory());
			p.sendMessage(Chat.PREFIX + "§bDas Lobbyinventar wurde gesetzt");
			return true;
		}
		printHelp(p);
		return true;
	}

}
