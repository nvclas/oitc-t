package de.theniclas.oitct.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.theniclas.oitct.commands.CMDac;
import de.theniclas.oitct.commands.CMDkit;
import de.theniclas.oitct.commands.CMDlobby;
import de.theniclas.oitct.commands.CMDmap;
import de.theniclas.oitct.commands.CMDping;
import de.theniclas.oitct.commands.CMDspec;
import de.theniclas.oitct.commands.CMDstartfight;
import de.theniclas.oitct.commands.CMDteam;
import de.theniclas.oitct.listeners.BlockBreakHandler;
import de.theniclas.oitct.listeners.BlockPlaceHandler;
import de.theniclas.oitct.listeners.EntityDamageByEntityHandler;
import de.theniclas.oitct.listeners.EntityDamageHandler;
import de.theniclas.oitct.listeners.FoodLevelChangeHandler;
import de.theniclas.oitct.listeners.PlayerChangedWorldHandler;
import de.theniclas.oitct.listeners.PlayerDropItemHandler;
import de.theniclas.oitct.listeners.PlayerInteractHandler;
import de.theniclas.oitct.listeners.PlayerJoinHandler;
import de.theniclas.oitct.listeners.PlayerMoveHandler;
import de.theniclas.oitct.listeners.PlayerPickupItemHandler;
import de.theniclas.oitct.listeners.PlayerQuitHandler;
import de.theniclas.oitct.utils.Data;

public class Main extends JavaPlugin {

	private static Main plugin;

	@Override
	public void onEnable() {

		plugin = this;

		//Commands
		this.getCommand("team").setExecutor(new CMDteam());
		this.getCommand("ac").setExecutor(new CMDac());
		this.getCommand("startfight").setExecutor(new CMDstartfight());
		this.getCommand("map").setExecutor(new CMDmap());
		this.getCommand("spec").setExecutor(new CMDspec());
		this.getCommand("kit").setExecutor(new CMDkit());
		this.getCommand("lobby").setExecutor(new CMDlobby());
		this.getCommand("ping").setExecutor(new CMDping());

		//Events
		Bukkit.getPluginManager().registerEvents(new PlayerMoveHandler(), this);
		Bukkit.getPluginManager().registerEvents(new EntityDamageHandler(), this);
		Bukkit.getPluginManager().registerEvents(new EntityDamageByEntityHandler(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitHandler(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerJoinHandler(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerChangedWorldHandler(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerInteractHandler(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDropItemHandler(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerPickupItemHandler(), this);
		Bukkit.getPluginManager().registerEvents(new FoodLevelChangeHandler(), this);
		Bukkit.getPluginManager().registerEvents(new BlockPlaceHandler(), this);
		Bukkit.getPluginManager().registerEvents(new BlockBreakHandler(), this);
		
		//Yaml configs
		new Data("tree.yml").createFile();
		new Data("maps.yml").createFile();
		new Data("kits.yml").createFile();
		new Data("lobby.yml").createFile();

		System.out.println("OITC-Turnierplugin läuft!");
	}

	public static Main getPlugin() {
		return plugin;
	}

}
