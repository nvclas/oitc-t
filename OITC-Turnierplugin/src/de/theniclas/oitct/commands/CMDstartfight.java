package de.theniclas.oitct.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.theniclas.oitct.objects.Kit;
import de.theniclas.oitct.objects.Map;
import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.objects.fight.Fight;
import de.theniclas.oitct.utils.Chat;

public class CMDstartfight implements CommandExecutor {

	private void printHelp(Player p) {
		p.sendMessage(Chat.PREFIX + "§8--- §7/§bstartfight §8---");
		p.sendMessage(Chat.PREFIX + "§e/startfight <Team> <Team> <Map> <Kit>");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!command.getName().equalsIgnoreCase("startfight")) return true;
		if(!(sender instanceof Player)) {
			sender.sendMessage("Dieser Befehl kann nur von Spielern genutzt werden");
			return true;
		}

		Player p = (Player) sender;
		if(!p.hasPermission("oitct.setup")) {
			p.sendMessage(Chat.NO_PERM);
			return true;
		}

		if(args.length < 4) {
			printHelp(p);
			return true;
		}
		
		if(args[0].equals(args[1])) {
			p.sendMessage(Chat.PREFIX + "§cEs müssen zwei unterschiedliche Teams angegeben werden");
			return true;
		}
		
		Team team1 = Team.getTeam(args[0]);
		Team team2 = Team.getTeam(args[1]);
		Map map = Map.getMap(args[2]);
		Kit kit = Kit.getKit(args[3]);
		if(team1 == null) {
			p.sendMessage(Chat.PREFIX + "§cDas Team §6" + args[0] + " §cexistiert nicht §8(§7/§bteam§8)");
			return true;
		}
		if(team2 == null) {
			p.sendMessage(Chat.PREFIX + "§cDas Team §6" + args[1] + " §cexistiert nicht §8(§7/§bteam§8)");
			return true;
		}
		if(map == null) {
			p.sendMessage(Chat.PREFIX + "§cDie Map §6" + args[2] + " §cexistiert nicht §8(§7/§bmap§8)");
			return true;
		}
		if(kit == null) {
			p.sendMessage(Chat.PREFIX + "§cDas Kit §6" + args[3] + " §cexistiert nicht §8(§7/§bkit§8)");
			return true;
		}
		
		if(team1.getFight() != null) {
			p.sendMessage(Chat.PREFIX + "§cDas Team §6" + team1.getTeamName() + " §ckämpft bereits");
			return true;
		}
		if(team2.getFight() != null) {
			p.sendMessage(Chat.PREFIX + "§cDas Team §6" + team2.getTeamName() + " §ckämpft bereits");
			return true;
		}
		
		if(map.getTeam1Spawns().size() < team1.getOnlineMembers().size()) {
			p.sendMessage(Chat.PREFIX + "§cDie Map hat nicht genug Spawnpunkte für §6" + team1.getTeamName() + " §c, es fehlen §6" + (team1.getMembers().size() - map.getTeam1Spawns().size()));
			return true;
		}
		if(map.getTeam2Spawns().size() < team2.getOnlineMembers().size()) {
			p.sendMessage(Chat.PREFIX + "§cDie Map hat nicht genug Spawnpunkte für §6" + team2.getTeamName() + " §c, es fehlen §6" + (team2.getMembers().size() - map.getTeam2Spawns().size()));
			return true;
		}
		if(team1.getOnlineMembers().size() == 0) {
			p.sendMessage(Chat.PREFIX + "§cNiemand aus §6" + team1.getTeamName() + " §cist online");
			return true;
		}
		if(team2.getOnlineMembers().size() == 0) {
			p.sendMessage(Chat.PREFIX + "§cNiemand aus §6" + team2.getTeamName() + " §cist online");
			return true;
		}
		Fight fight = new Fight(team1, team2, map, kit);
		fight.start();
		
		return true;
	}

}
