package de.theniclas.oitct.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.objects.fight.Fight;

public class AsyncPlayerChatHandler implements Listener {
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		Team team = Team.getTeam(Team.getTeamName(p.getUniqueId().toString()));
		e.setFormat("");
		for(Player all : Bukkit.getOnlinePlayers()) {
			if(team == null || team.getFight() == null && Fight.getSpectatingFight(p) == null)
				all.sendMessage("§a" + p.getName() + "§8: §7" + e.getMessage());
		}
		if(team == null || team.getFight() == null && Fight.getSpectatingFight(p) == null) return;
		for(Player witness : team.getFight().getWitnesses()) {
			witness.sendMessage("§c" + p.getName() + "§8: §7" + e.getMessage());
		}
	}
}
