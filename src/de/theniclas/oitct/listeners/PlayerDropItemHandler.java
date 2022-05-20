package de.theniclas.oitct.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.objects.fight.State;

public class PlayerDropItemHandler implements Listener {

	@EventHandler
	public void handleItemDrop(PlayerDropItemEvent e) {
		
		e.setCancelled(true);
		
		Player p = e.getPlayer();
		Team team = Team.getTeam(p.getUniqueId());
		
		if(team != null && team.getFight() != null && team.getFight().getState() == State.ONGOING && team.getFight().getAlive().contains(p)) {
			e.setCancelled(false);
			return;
		}
		
		if(p.hasPermission("oitct.build")) {
			if(team != null && team.getFight() != null && team.getFight().getState() != State.ONGOING && team.getFight().getAlive().contains(p)) {
				return;
			}
			e.setCancelled(false);
			return;
		}
		
	}
}
