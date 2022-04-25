package de.theniclas.oitct.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import de.theniclas.oitct.utils.Data;

public class Map {

	private String name;
	private List<Location> team1Spawns;
	private List<Location> team2Spawns;
	private World world;
	public static final Data MAPS = new Data("maps.yml");

	private Map(String name) {
		this.name = name;
		this.team1Spawns = new ArrayList<>();
		this.team2Spawns = new ArrayList<>();
	}

	public Map(String name, World world) {
		this.name = name;
		this.world = world;
		this.team1Spawns = new ArrayList<>();
		this.team2Spawns = new ArrayList<>();
	}

	public void saveMap() {
		MAPS.getConfig().set("Map." + name + ".World", getWorld().getName());
		List<String> locList1 = new ArrayList<>();
		for(Location loc : getTeam1Spawns()) {
			locList1.add(getStringFromLocation(loc));
		}
		MAPS.getConfig().set("Map." + name + ".Spawns1", locList1);
		List<String> locList2 = new ArrayList<>();
		for(Location loc : getTeam2Spawns()) {
			locList2.add(getStringFromLocation(loc));
		}
		MAPS.getConfig().set("Map." + name + ".Spawns2", locList2);
		MAPS.saveFile();
	}
	
	public void deleteMap() {
		MAPS.getConfig().set("Map." + name, null);
		MAPS.saveFile();
	}

	public boolean exists() {
		if(MAPS.getConfig().get("Map." + name) == null || MAPS.getConfig().get("Map." + name + ".World") == null) {
			return false;
		}
		return true;
	}

	public void addTeam1Spawn(Location location) {
		location.setWorld(world);
		team1Spawns.add(location);
	}

	public void addTeam2Spawn(Location location) {
		location.setWorld(world);
		team2Spawns.add(location);
	}

	public List<Location> getTeam1Spawns() {
		for(Location loc : team1Spawns) {
			loc.setWorld(world);
		}
		return team1Spawns;
	}

	public List<Location> getTeam2Spawns() {
		for(Location loc : team2Spawns) {
			loc.setWorld(world);
		}
		return team2Spawns;
	}

	public World getWorld() {
		return world;
	}

	public String getName() {
		return name;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public static Location getLocationFromString(World w, String s) {
		if(s == null || s.trim() == "") {
			return null;
		}
		String[] parts = s.split(", ");
		if(parts.length == 4) {
			float x = Float.parseFloat(parts[0]);
			float y = Float.parseFloat(parts[1]);
			float z = Float.parseFloat(parts[2]);
			float yaw = Float.parseFloat(parts[3]);
			return new Location(w, x, y, z, yaw, 0);
		}
		return null;
	}

	static public String getStringFromLocation(Location location) {
		return (location.getBlockX() + 0.5) + ", " + location.getBlockY() + ", " + (location.getBlockZ() + 0.5) + ", " + Math.round(location.getYaw());
	}

	public static List<Map> getMapList() {
		List<Map> maps = new ArrayList<>();
		if(MAPS.getConfig().getConfigurationSection("Map") == null || MAPS.getConfig().getConfigurationSection("Map").getKeys(false).isEmpty()) {
			return maps;
		}
		for(String mapName : MAPS.getConfig().getConfigurationSection("Map").getKeys(false)) {
			if(MAPS.getConfig().get("Map." + mapName + ".World") != null) {
				maps.add(getMap(mapName));
			}
		}
		return maps;
	}
	
	public static Map getMap(String name) {
		Map map = new Map(name);
		if(map.exists()) {
			map.setWorld(Bukkit.getWorld(MAPS.getConfig().getString("Map." + map.getName() + ".World")));
			for(String locString : MAPS.getConfig().getStringList("Map." + map.getName() + ".Spawns1")) {
				map.addTeam1Spawn(getLocationFromString(map.getWorld(), locString));
			}
			for(String locString : MAPS.getConfig().getStringList("Map." + map.getName() + ".Spawns2")) {
				map.addTeam2Spawn(getLocationFromString(map.getWorld(), locString));
			}
			return map;
		}
		return null;
	}

}
