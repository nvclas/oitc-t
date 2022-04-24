package de.theniclas.oitct.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;

import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.objects.fight.State;
import de.theniclas.oitct.utils.Chat;

public class EntityDamageHandler implements Listener {
	
	@EventHandler
	public void handleEntityDamage(EntityDamageEvent e) {
		
		if(!(e.getEntity() instanceof Player)) {
			e.setCancelled(true);
			return;
		}
		Player p = (Player) e.getEntity();
		Team team = Team.getTeam(Team.getTeamName(p.getUniqueId().toString()));
		 
		if(team == null || team.getFight() == null || team.getFight().getState() != State.ONGOING || !team.getFight().getAlive().contains(p)) {
			e.setCancelled(true);
			return;
		}
		if(p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
			e.setCancelled(true);
			return;
		}
		if(e.getFinalDamage() >= p.getHealth()) {
			e.setCancelled(true);
			if(e.getCause() != DamageCause.PROJECTILE && e.getCause() != DamageCause.ENTITY_ATTACK) {
				for(Player witness : team.getFight().getWitnesses()) {
					witness.sendMessage(Chat.PREFIX + "§e" + p.getName() + " §bist gestorben");
				}
				team.getFight().kill(p);
				return;
			}
		}
	}
}
