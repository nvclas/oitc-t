package de.theniclas.oitct.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import de.theniclas.oitct.objects.Lobby;
import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.objects.fight.Fight;

public class PlayerChangedWorldHandler implements Listener {
	
	@EventHandler
	public void handlePlayerChangedWorld(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		
		if(Fight.getSpectatingFight(p) != null) {
			Fight.getSpectatingFight(p).removeSpectator(p);
		}
		Team team = Team.getTeam(Team.getTeamName(p.getUniqueId().toString()));
		if(team == null || team.getFight() == null) {
			return;
		}
		if(team.getFight().getMap().getWorld() == p.getWorld() || Lobby.getSpawn().getWorld() == p.getWorld()) {
			return;
		}
		if(team.getFight().getAlive().contains(p)) {
			team.getFight().disqualify(p);
			return;
		}
	}
}
