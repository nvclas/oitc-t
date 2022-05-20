package de.theniclas.oitct.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import de.theniclas.oitct.main.Main;

public class Data {

	private String fileName;
	private File file;
	private FileConfiguration config;

	public Data(JavaPlugin plugin, String fileName) {
		this.fileName = fileName;
		this.file = new File(plugin.getDataFolder() + "/" + fileName);
		this.config = YamlConfiguration.loadConfiguration(file);
	}

	public String getFileName() {
		return fileName;
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public void createFile() {
		if(!file.exists()) {
			Main.getPlugin().getDataFolder().mkdir();
			try {
				file.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
			System.out.println("Config erstellt");
		}
	}

	public void saveFile() {
		try {
			config.save(file);
		} catch(IOException e) {
			e.printStackTrace();
		}
		config = YamlConfiguration.loadConfiguration(file);
	}

}
