package de.theniclas.oitct.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.theniclas.oitct.utils.Data;
import de.theniclas.oitct.utils.UtilityMethods;

public class Lobby {

	private static List<Player> players;
	public static final Data LOBBY = new Data("lobby.yml");
	
	private Lobby() {
	}
	
	private static class LobbyHolder {
		private static final Lobby LOBBY = new Lobby();
	}
	
	public static Lobby getLobby() {
		return LobbyHolder.LOBBY;
	}
	
	public void sendToLobby(Player p) {
		p.setGameMode(GameMode.ADVENTURE);
		UtilityMethods.fullHeal(p);
		p.teleport(getSpawn());
		p.getInventory().setContents(getInventory().getContents());
		p.getInventory().setArmorContents(null);
		for(Player all : Bukkit.getOnlinePlayers()) {
			p.showPlayer(all);
		}
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public Location getSpawn() {
		World world = Bukkit.getWorld(LOBBY.getConfig().getString("Lobby.World"));
		return Map.getLocationFromString(world, LOBBY.getConfig().getString("Lobby.Spawn"));
	}
	
	public Inventory getInventory() {
		Inventory inv = Bukkit.createInventory(null, 4*9);
		inv.setContents(LOBBY.getConfig().getList("Lobby.Inventory").toArray(new ItemStack[0]));
		return inv;
	}
	
	public void setSpawn(Location spawn) {
		LOBBY.getConfig().set("Lobby.Spawn", Map.getStringFromLocation(spawn));
		LOBBY.getConfig().set("Lobby.World", spawn.getWorld().getName());
		LOBBY.saveFile();
	}
	
	public void setInventory(Inventory inventory) {
		List<ItemStack> itemList = new ArrayList<>();
		for(ItemStack is : inventory.getContents()) {
			if(is == null) is = new ItemStack(Material.AIR);
			itemList.add(is);
		}
		LOBBY.getConfig().set("Lobby.Inventory", itemList);
		LOBBY.saveFile();
	}
	
}
