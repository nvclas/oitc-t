package de.theniclas.oitct.listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.objects.fight.State;

public class FoodLevelChangeHandler implements Listener {

	@EventHandler
	public void handleFoodLevelChange(FoodLevelChangeEvent e) {
		e.setCancelled(true);
		
		if(!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		Team team = Team.getTeam(Team.getTeamName(p.getUniqueId().toString()));
		
		if(team == null || team.getFight() == null || team.getFight().getState() != State.ONGOING || !team.getFight().getAlive().contains(p)) {
			return;
		}
		
		List<ItemStack> list = Arrays.asList(team.getFight().getKit().getInventory().getContents());
		if(list.stream().anyMatch(item -> item != null && item.getType().isEdible())){
			e.setCancelled(false);
		}
	}
}
