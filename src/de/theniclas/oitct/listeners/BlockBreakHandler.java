package de.theniclas.oitct.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.objects.fight.State;

public class BlockBreakHandler implements Listener {
	
	@EventHandler
	public void handleBlockBreak(BlockBreakEvent e) {
		
		Player p = e.getPlayer();
		Team team = Team.getTeam(p.getUniqueId());
		
		e.setCancelled(true);
		
		if(team != null && team.getFight() != null && team.getFight().getState() == State.ONGOING && team.getFight().getAlive().contains(p)) {
			if(!team.getFight().getPlacedBlocks().contains(e.getBlock())) {
				return;
			}
			e.setCancelled(false);
			team.getFight().getPlacedBlocks().remove(e.getBlock());
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
