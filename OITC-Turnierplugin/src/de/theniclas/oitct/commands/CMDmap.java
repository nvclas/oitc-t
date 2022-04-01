package de.theniclas.oitct.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.theniclas.oitct.objects.Map;
import de.theniclas.oitct.utils.Chat;

public class CMDmap implements CommandExecutor {

	private void printHelp(Player p) {
		p.sendMessage(Chat.PREFIX + "§8--- §7/§bmap §8---");
		p.sendMessage(Chat.PREFIX + "§e/map list");
		p.sendMessage(Chat.PREFIX + "§e/map add <Mapname>");
		p.sendMessage(Chat.PREFIX + "§e/map remove <Mapname>");
		p.sendMessage(Chat.PREFIX + "§e/map add1spawn <Mapname>");
		p.sendMessage(Chat.PREFIX + "§e/map add2spawn <Mapname>");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!command.getName().equalsIgnoreCase("map")) return true;
		if(!(sender instanceof Player)) {
			sender.sendMessage("Dieser Befehl kann nur von Spielern genutzt werden");
			return true;
		}

		Player p = (Player) sender;
		if(!p.hasPermission("oitct.setup")) {
			p.sendMessage(Chat.NO_PERM);
			return true;
		}
		
		if(args.length == 0) {
			printHelp(p);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("list")) {
			if(Map.getMapList().isEmpty()) {
				p.sendMessage(Chat.PREFIX + "§bEs gibt noch keine Maps");
				return true;
			}
			p.sendMessage(Chat.PREFIX + "§7--- §bListe aller Maps §7---");
			for(Map map : Map.getMapList()) {
				p.sendMessage(Chat.PREFIX + "§7- §d" + map.getName());
			}
			return true;
		}
		
		if(args.length <= 1) {
			printHelp(p);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("add")) {
			Map map = new Map(args[1], p.getWorld());
			if(map.exists()) {
				p.sendMessage(Chat.PREFIX + "§cDiese Map gibt es bereits");
				return true;
			}
			map.saveMap();
			p.sendMessage(Chat.PREFIX + "§bDie Map §e" + args[1] + " §bwurde hinzugefügt");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("remove")) {
			Map map = Map.getMap(args[1]);
			if(map == null) {
				p.sendMessage(Chat.PREFIX + "§cDiese Map gibt es nicht");
				return true;
			}
			map.deleteMap();
			p.sendMessage(Chat.PREFIX + "§bDie Map §e" + args[1] + " wurde gelöscht");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("add1spawn")) {
			Map map = Map.getMap(args[1]);
			if(map == null) {
				p.sendMessage(Chat.PREFIX + "§cDiese Map gibt es nicht");
				return true;
			}
			map.addTeam1Spawn(p.getLocation());
			map.saveMap();
			p.sendMessage(Chat.PREFIX + "§bSpawnpunkt für Team 1 wurde hinzugefügt");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("add2spawn")) {
			Map map = Map.getMap(args[1]);
			if(map == null) {
				p.sendMessage(Chat.PREFIX + "§cDiese Map gibt es nicht");
				return true;
			}
			map.addTeam2Spawn(p.getLocation());
			map.saveMap();
			p.sendMessage(Chat.PREFIX + "§bSpawnpunkt für Team 2 wurde hinzugefügt");
			return true;
		}
		printHelp(p);
		return true;
	}

}
