package de.theniclas.oitct.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.objects.fight.State;

public class PlayerMoveHandler implements Listener {
	
	@EventHandler
	public void handlePlayerMove(PlayerMoveEvent e) {

		Player p = e.getPlayer();
		
		Team team = Team.getTeam(p.getUniqueId());
		if(team == null || team.getFight() == null) {
			return;
		}
		if(team.getFight().getState() != State.STARTING) {
			return;
		}
		if(e.getTo().getX() != e.getFrom().getX() || e.getTo().getZ() != e.getFrom().getZ()) {
			e.setTo(e.getFrom());
		}
	}
}
