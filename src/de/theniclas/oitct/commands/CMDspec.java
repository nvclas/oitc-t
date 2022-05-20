package de.theniclas.oitct.commands;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.objects.fight.Fight;
import de.theniclas.oitct.utils.Chat;

public class CMDspec implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!command.getName().equalsIgnoreCase("spec")) return true;
		if(!(sender instanceof Player)) {
			sender.sendMessage("Dieser Befehl kann nur von Spielern genutzt werden");
			return true;
		}
		Player p = (Player) sender;	
		
		if(args.length == 0) {
			p.sendMessage(Chat.PREFIX + "§e/spec <Team>");
			return true;
		}
		Team spec = Team.getTeam(args[0]);
		if(spec == null) {
			p.sendMessage(Chat.PREFIX + "§cDieses Team existiert nicht");
			return true;
		}
		if(spec.getFight() == null) {
			p.sendMessage(Chat.PREFIX + "§cDieses Team kämpft gerade nicht");
			return true;
		}
		Team team = Team.getTeam(p.getUniqueId());
		if(team != null && team.getFight() != null && team.getFight().getAlive().contains(p)) {
			p.sendMessage(Chat.PREFIX + "§cDu bist mitten im Kampf");
			return true;
		}
		if(Fight.getSpectatingFight(p) != null) {
			Fight.getSpectatingFight(p).removeSpectator(p);
		}
		spec.getFight().addSpectator(p);
		
		World world = spec.getFight().getMap().getWorld();
		double x1 = spec.getFight().getMap().getTeam1Spawns().get(0).getX();
		double x2 = spec.getFight().getMap().getTeam2Spawns().get(0).getX();
		double z1 = spec.getFight().getMap().getTeam1Spawns().get(0).getZ();
		double z2 = spec.getFight().getMap().getTeam2Spawns().get(0).getZ();
		Location specSpawn = new Location(world, (x1+x2)/2, world.getHighestBlockYAt((int) (x1+x2)/2, (int) (z1+z2)/2) + 1, (z1+z2)/2);
		p.teleport(specSpawn);
		p.setGameMode(GameMode.SPECTATOR);
		p.sendMessage(Chat.PREFIX + "§aDu schaust nun dem Kampf §e" + spec.getFight().getTeam1().getTeamName() + " §cvs§7. §e" + spec.getFight().getTeam2().getTeamName() + " §azu");
		
		return true;
	}
	
}
