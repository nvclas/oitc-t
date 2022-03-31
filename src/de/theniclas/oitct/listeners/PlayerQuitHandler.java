package de.theniclas.oitct.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.objects.fight.Fight;

public class PlayerQuitHandler implements Listener {
	
	@EventHandler
	public void handlePlayerDisconnect(PlayerQuitEvent e) {
		
		e.setQuitMessage("");
		
		Player p = e.getPlayer();
		
		Team team = Team.getTeam(Team.getTeamName(p.getUniqueId().toString()));
		
		if(Fight.getSpectatingFight(p) != null) {
			Fight.getSpectatingFight(p).removeSpectator(p);
		}
		
		if(team == null || team.getFight() == null || !team.getFight().getAlive().contains(p)) {
			return;
		}
		team.getFight().disqualify(p);
		
	}
}
