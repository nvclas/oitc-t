package de.theniclas.oitct.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.utils.Chat;

public class EntityDamageByEntityHandler implements Listener {

	@EventHandler
	public void handleEntityDamageByEntity(EntityDamageByEntityEvent e) {
		
		if(!(e.getEntity() instanceof Player)) return;
		
		Player p = (Player) e.getEntity();
		
		Team team = Team.getTeam(Team.getTeamName(p.getUniqueId().toString()));
		
		if(team == null) {
			return;
		}
		
		if(e.getFinalDamage() >= p.getHealth()) {
			e.setCancelled(true);
			for(Player witness : team.getFight().getWitnesses()) {
				if(!(e.getDamager() instanceof Projectile)) {
					witness.sendMessage(Chat.PREFIX + "§e" + p.getName() + " §bwurde von §e" + e.getDamager().getName() + " §bgetötet");
					continue;
				}
				witness.sendMessage(Chat.PREFIX + "§e" + p.getName() + " §bwurde von §e" + ((Entity) ((Projectile) e.getDamager()).getShooter()).getName() + " §bgetötet");
			}
			team.getFight().kill(p);
			return;
		}
		
	}
}
