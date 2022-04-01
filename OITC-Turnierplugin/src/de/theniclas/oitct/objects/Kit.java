package de.theniclas.oitct.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.theniclas.oitct.utils.Data;

public class Kit {

	private String name;
	private Inventory inventory;
	private ItemStack[] armor;
	public static final Data KITS = new Data("kits.yml");
	
	public Kit(String name, Inventory inventory, ItemStack[] armor) {
		this.name = name;
		this.inventory = inventory;
		this.armor = armor;
	}

	private Kit(String name) {
		this.name = name;
	}
	
	public void saveKit() {
		List<ItemStack> itemList = new ArrayList<>();
		for(ItemStack is : inventory.getContents()) {
			if(is == null) is = new ItemStack(Material.AIR);
			itemList.add(is);
		}
		KITS.getConfig().set("Kit." + name + ".Inventory", itemList);
		KITS.getConfig().set("Kit." + name + ".Armor", armor);
		KITS.saveFile();
	}
	
	public void deleteKit() {
		KITS.getConfig().set("Kit." + name, null);
		KITS.saveFile();
	}
	
	public boolean exists() {
		if(KITS.getConfig().get("Kit." + name) == null) {
			return false;
		}
		return true;
	}
	
	public void giveKit(Player p) {
		p.getInventory().setContents(inventory.getContents());
		p.getInventory().setArmorContents(armor);
		p.updateInventory();
	}
	
	public static Kit getKit(String name) {
		Kit kit = new Kit(name);
		if(kit.exists()) {
			Inventory inv = Bukkit.createInventory(null, 4*9);
			inv.setContents(KITS.getConfig().getList("Kit." + name + ".Inventory").toArray(new ItemStack[0]));
			ItemStack[] armor = KITS.getConfig().getList("Kit." + name + ".Armor").toArray(new ItemStack[0]);
			kit.setInventory(inv);
			kit.setArmor(armor);
			return kit;
		}
		return null;
	}
	
	public static List<Kit> getKitList() {
		List<Kit> kits = new ArrayList<>();
		if(KITS.getConfig().getConfigurationSection("Kit") == null || KITS.getConfig().getConfigurationSection("Kit").getKeys(false).isEmpty()) {
			return kits;
		}
		for(String kitName : KITS.getConfig().getConfigurationSection("Kit").getKeys(false)) {
			kits.add(getKit(kitName));
		}
		return kits;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public ItemStack[] getArmor() {
		return armor;
	}
	
	public String getName() {
		return name;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	public void setArmor(ItemStack[] armor) {
		this.armor = armor;
	}
	
}
