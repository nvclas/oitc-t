package de.theniclas.oitct.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.utils.Chat;
import de.theniclas.oitct.utils.Methods;

public class CMDteam implements CommandExecutor {

	private void printHelp(Player p) {
		if(p.hasPermission("oitct.setup")) {
			p.sendMessage(Chat.PREFIX + "§8--- §7/§bteam §8---");
			p.sendMessage(Chat.PREFIX + "§e/team create <Spieler>");
			p.sendMessage(Chat.PREFIX + "§e/team create <Teamname> <Spieler> [Spieler] ... [Spieler]");
			p.sendMessage(Chat.PREFIX + "§e/team delete <Teamname>");
			p.sendMessage(Chat.PREFIX + "§e/team rename <Teamname> <neuer Teamname>");
			p.sendMessage(Chat.PREFIX + "§e/team addpoints <Teamname> <Punkte>");
			p.sendMessage(Chat.PREFIX + "§e/team remove <Teamname> <Spieler>");
			p.sendMessage(Chat.PREFIX + "§e/team add <Teamname> <Spieler>");
		}
		p.sendMessage(Chat.PREFIX + "§e/team list");
		p.sendMessage(Chat.PREFIX + "§e/team show");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!command.getName().equalsIgnoreCase("team")) return true;
		if(!(sender instanceof Player)) {
			sender.sendMessage("Dieser Befehl kann nur von Spielern genutzt werden");
			return true;
		}
		
		Player p = (Player) sender;	
		if(args.length == 1 && args[0].equalsIgnoreCase("list")) {
			if(Team.getTeamList().isEmpty()) {
				p.sendMessage(Chat.PREFIX + "§bEs gibt noch keine Teams");
				return true;
			}
			p.sendMessage(Chat.PREFIX + "§7--- §bListe aller Teams §7---");
			for(Team team : Team.getTeamList()) {
				p.sendMessage(Chat.PREFIX + "§c" + team.getTeamName() + "§8: §7(§9" + team.getPoints() + "§7)");
				for(String uuid : team.getMembers()) {
					p.sendMessage(Chat.PREFIX + "§7- §d" + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName());
				}
				p.sendMessage(Chat.PREFIX + "");
			}
			return true;
		}
		
		if(args.length == 1 && args[0].equalsIgnoreCase("show")) {
			if(!Team.hasTeam(p.getUniqueId().toString())) {
				p.sendMessage(Chat.PREFIX + "§cDu bist in keinem Team");
				return true;
			}
			Team team = Team.getTeam(Team.getTeamName(p.getUniqueId().toString()));
			p.sendMessage(Chat.PREFIX + "§cDein Team §e" + team.getTeamName() + "§8: §7(§9" + team.getPoints() + "§7)");
			for(String uuid : team.getMembers()) {
				p.sendMessage(Chat.PREFIX + "§7- §d" + Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName());
			}
			return true;
		}

		if(!p.hasPermission("oitct.setup")) {
			p.sendMessage(Chat.NO_PERM);
			return true;
		}

		if(args.length == 2) {

			if(args[0].equalsIgnoreCase("create")) {
				OfflinePlayer target = Bukkit.getOfflinePlayer(Methods.getOfflineUUID(args[1]));
				if(!target.hasPlayedBefore()) {
					p.sendMessage(Chat.PREFIX + "§cDieser Spieler war noch nie online");
					return true;
				}
				if(Team.hasTeam(target.getUniqueId().toString())) {
					p.sendMessage(Chat.PREFIX + "§cDer Spieler ist bereits im Team §6" + Team.getTeamName(target.getUniqueId().toString()));
					return true;
				}

				Team team = new Team(target.getName());
				team.addMember(target.getUniqueId().toString());
				team.saveTeam();
				p.sendMessage(Chat.PREFIX + "§bDas Team §e" + team.getTeamName() + " §bwurde erstellt");
				p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 0.5f, 1.0f);
				if(target.isOnline()) {
					((Player) target).sendMessage(Chat.PREFIX + "§bDu bist nun im Team §e" + team.getTeamName());
					((Player) target).playSound(((Player) target).getLocation(), Sound.SUCCESSFUL_HIT, 0.5f, 1.0f);
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("delete")) {
				Team team = Team.getTeam(args[1]);
				if(team == null) {
					p.sendMessage(Chat.PREFIX + "§cDas Team existiert nicht");
					return true;
				}
				if(team.getFight() != null) {
					p.sendMessage(Chat.PREFIX + "§cDu kannst ein Team nicht bearbeiten während es kämpft");
					return true;
				}
				team.deleteTeam();
				p.sendMessage(Chat.PREFIX + "§bDas Team §e" + team.getTeamName() + " §bwurde gelöscht");
				p.playSound(p.getLocation(), Sound.BAT_DEATH, 0.5f, 1.0f);
				for(String uuid : team.getMembers()) {
					if(!Bukkit.getOfflinePlayer(UUID.fromString(uuid)).isOnline()) {
						continue;
					}
					Player target = Bukkit.getPlayer(UUID.fromString(uuid));
					target.sendMessage(Chat.PREFIX + "§cDein Team wurde gelöscht");
					target.playSound(target.getLocation(), Sound.BAT_DEATH, 0.5f, 1.0f);
				}
				return true;
			}
			printHelp(p);
			return true;
		}
		if(args.length >= 3) {

			if(args[0].equalsIgnoreCase("remove")) {
				Team team = Team.getTeam(args[1]);
				if(team == null) {
					p.sendMessage(Chat.PREFIX + "§cDas Team existiert nicht");
					return true;
				}
				if(team.getFight() != null) {
					p.sendMessage(Chat.PREFIX + "§cDu kannst ein Team nicht bearbeiten während es kämpft");
					return true;
				}
				OfflinePlayer target = Bukkit.getOfflinePlayer(Methods.getOfflineUUID(args[2]));
				if(!target.hasPlayedBefore()) {
					p.sendMessage(Chat.PREFIX + "§cDieser Spieler war noch nie online");
					return true;
				}
				if(!team.getTeamName().equals(Team.getTeamName(target.getUniqueId().toString()))) {
					p.sendMessage(Chat.PREFIX + "§cDer Spieler ist in Team §6" + Team.getTeamName(target.getUniqueId().toString()));
					return true;
				}
				team.sendAllMembers(Chat.PREFIX + "§e" + target.getName() + " §bwurde aus §e" + team.getTeamName() + " §bentfernt");
				team.removeMember(target.getUniqueId().toString());
				p.sendMessage(Chat.PREFIX + "§bDer Spieler §e" + target.getName() + " §bwurde aus §e" + team.getTeamName() + " §bentfernt");
				if(team.getMembers().isEmpty()) {
					team.deleteTeam();
					p.sendMessage(Chat.PREFIX + "§bDas Team hatte keine Spieler mehr und wurde gelöscht");
					return true;
				}
				team.saveTeam();
				return true;
			}
			
			if(args[0].equalsIgnoreCase("addpoints")) {
				Team team = Team.getTeam(args[1]);
				if(team == null) {
					p.sendMessage(Chat.PREFIX + "§cDas Team existiert nicht");
					return true;
				}
				if(team.getFight() != null) {
					p.sendMessage(Chat.PREFIX + "§cDu kannst ein Team nicht bearbeiten während es kämpft");
					return true;
				}
				if(!Methods.isNumeric(args[2]) && Integer.parseInt(args[2]) > 0) {
					p.sendMessage(Chat.PREFIX + "§cDu musst eine ganze Zahl, die größer als 0 ist angeben");
					return true;
				}
				int points = Integer.parseInt(args[2]);
				team.addPoints(points);
				if(points == 1) {
					p.sendMessage(Chat.PREFIX + "§bDem Team §e" + team.getTeamName() + " §bwurde §e" + args[2] + " §bPunkt hinzugefügt");
					return true;
				}
				p.sendMessage(Chat.PREFIX + "§bDem Team §e" + team.getTeamName() + " §bwurden §e" + args[2] + " §bPunkte hinzugefügt");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("add")) {
				Team team = Team.getTeam(args[1]);
				if(team == null) {
					p.sendMessage(Chat.PREFIX + "§cDas Team existiert nicht");
					return true;
				}
				if(team.getFight() != null) {
					p.sendMessage(Chat.PREFIX + "§cDu kannst ein Team nicht bearbeiten während es kämpft");
					return true;
				}
				OfflinePlayer target = Bukkit.getOfflinePlayer(Methods.getOfflineUUID(args[2]));
				if(!target.hasPlayedBefore()) {
					p.sendMessage(Chat.PREFIX + "§cDieser Spieler war noch nie online");
					return true;
				}
				if(Team.hasTeam(target.getUniqueId().toString())) {
					p.sendMessage(Chat.PREFIX + "§cDer Spieler ist bereits im Team §6" + Team.getTeamName(target.getUniqueId().toString()));
					return true;
				}
				team.addMember(target.getUniqueId().toString());
				team.saveTeam();
				p.sendMessage(Chat.PREFIX + "§bDer Spieler §e" + target.getName() + " §bwurde zu §e" + team.getTeamName() + " §bhinzugefügt");
				team.sendAllMembers(Chat.PREFIX + "§e" + target.getName() + " §bwurde §e" + team.getTeamName() + " §bhinzugefügt");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("rename")) {
				Team team = Team.getTeam(args[1]);
				if(team == null) {
					p.sendMessage(Chat.PREFIX + "§cDas Team existiert nicht");
					return true;
				}
				if(team.getFight() != null) {
					p.sendMessage(Chat.PREFIX + "§cDu kannst ein Team nicht bearbeiten während es kämpft");
					return true;
				}
				if(team.getTeamName().equals(args[2])) {
					p.sendMessage(Chat.PREFIX + "§cDas Team heißt bereits so");
					return true;
				}
				Team newTeam = new Team(args[2]);
				if(newTeam.exists()) {
					p.sendMessage(Chat.PREFIX + "§cEs gibt bereits ein Team mit diesem Namen");
					return true;
				}
				newTeam.setMembers(team.getMembers());
				newTeam.saveTeam();
				team.deleteTeam();
				p.sendMessage(Chat.PREFIX + "§bDas Team §e" + team.getTeamName() + " §bwurde zu §e" + newTeam.getTeamName() + " §bumbenannt");
				newTeam.sendAllMembers(Chat.PREFIX + "§bDein Team wurde zu §e" + newTeam.getTeamName() + " §bumbenannt");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("create")) {
				Team team = new Team(args[1]);
				if(team.exists()) {
					p.sendMessage(Chat.PREFIX + "§cEs gibt bereits ein Team mit diesem Namen");
					return true;
				}
				for(int i = 2; i < args.length; i++) {
					OfflinePlayer target = Bukkit.getOfflinePlayer(Methods.getOfflineUUID(args[i]));
					if(!target.hasPlayedBefore()) {
						p.sendMessage(Chat.PREFIX + "§6" + target.getName() + " §cwar noch nie online");
						return true;
					}
					if(Team.hasTeam(target.getUniqueId().toString())) {
						p.sendMessage(Chat.PREFIX + "§6" + target.getName() + " §cist bereits im Team §6" + Team.getTeamName(target.getUniqueId().toString()));
						return true;
					}
					team.addMember(target.getUniqueId().toString());
				}
				team.saveTeam();
				p.sendMessage(Chat.PREFIX + "§bDas Team §e" + team.getTeamName() + " §bwurde erstellt");
				p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 0.5f, 1.0f);
				for(String uuid : team.getMembers()) {
					OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
					if(target.isOnline()) {
						((Player) target).sendMessage(Chat.PREFIX + "§bDu bist nun im Team §e" + team.getTeamName());
						((Player) target).playSound(((Player) target).getLocation(), Sound.SUCCESSFUL_HIT, 0.5f, 1.0f);
					}
				}
				return true;
			}
			printHelp(p);
			return true;
		}
		printHelp(p);
		return true;
	}
}
