package de.theniclas.oitct.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.theniclas.oitct.objects.Lobby;

public class PlayerJoinHandler implements Listener {

	@EventHandler
	public void handlePlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		e.setJoinMessage("");
		Lobby.sendToLobby(p);
	}
	
}
