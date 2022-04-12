package de.theniclas.oitct.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.utils.Chat;

public class EntityDamageByEntityHandler implements Listener {

	@EventHandler
	public void handleEntityDamageByEntity(EntityDamageByEntityEvent e) {
		
		if(!(e.getEntity() instanceof Player)) return;
		if(!(e.getDamager() instanceof Player)) return;
		
		Player p = (Player) e.getEntity();
		Player killer = (Player) e.getDamager();
		
		Team team = Team.getTeam(Team.getTeamName(p.getUniqueId().toString()));
		
		if(team == null) {
			return;
		}
		
		if(e.getFinalDamage() >= p.getHealth()) {
			e.setCancelled(true);
			for(Player witness : team.getFight().getWitnesses()) {
				witness.sendMessage(Chat.PREFIX + "§e" + p.getName() + " §bwurde von §e" + killer.getName() + " §bgetötet");
			}
			team.getFight().kill(p);
			return;
		}
		
	}
}
