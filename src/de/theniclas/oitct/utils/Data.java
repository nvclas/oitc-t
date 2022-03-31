package de.theniclas.oitct.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.theniclas.oitct.main.Main;

public class Data {

	private final String path = "plugins/OITC-T/";
	private String fileName;
	private File file;
	private FileConfiguration config;

	public Data(String fileName) {
		this.fileName = fileName.replace(".yml", "") + ".yml";
		this.file = new File(path + fileName);
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
