package de.theniclas.oitct.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Methods {

	public static UUID getOfflineUUID(String name) {
		for(OfflinePlayer op : Bukkit.getOfflinePlayers()) {
			if(op.getName().equalsIgnoreCase(name)) {
				return op.getUniqueId();
			}
		}
		return UUID.fromString("00000000-0000-0000-0000-000000000000");
	}
	
	public static boolean isNumeric(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	public static void fullHeal(Player p) {
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setSaturation(0);
		p.getActivePotionEffects().clear();
		p.setFireTicks(0);
		p.setFallDistance(0);
	}
	
}
