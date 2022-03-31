package de.theniclas.oitct.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.objects.fight.State;

public class BlockPlaceHandler implements Listener {
	
	@EventHandler
	public void handleBlockPlace(BlockPlaceEvent e) {
		
		Player p = e.getPlayer();
		Team team = Team.getTeam(Team.getTeamName(p.getUniqueId().toString()));
		
		e.setCancelled(true);
		
		if(team != null && team.getFight() != null && team.getFight().getState() == State.ONGOING && team.getFight().getAlive().contains(p)) {
			e.setCancelled(false);
			team.getFight().getPlacedBlocks().add(e.getBlock());
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
