package de.theniclas.oitct.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class UUIDFetcher {

	public static UUID getOfflineUUID(String name) {
		for(OfflinePlayer op : Bukkit.getOfflinePlayers()) {
			if(op.getName().equalsIgnoreCase(name)) {
				return op.getUniqueId();
			}
		}
		return UUID.fromString("00000000-0000-0000-0000-000000000000");
	}
	
}
