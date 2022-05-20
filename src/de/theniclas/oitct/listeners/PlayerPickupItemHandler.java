package de.theniclas.oitct.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.objects.fight.State;

public class PlayerPickupItemHandler implements Listener {
	
	@EventHandler
	public void handleItemPickup(PlayerPickupItemEvent e) {
		
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
