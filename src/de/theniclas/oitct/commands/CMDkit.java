package de.theniclas.oitct.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.theniclas.oitct.objects.Kit;
import de.theniclas.oitct.utils.Chat;

public class CMDkit implements CommandExecutor {

	private void printHelp(Player p) {
		p.sendMessage(Chat.PREFIX + "§8--- §7/§bkit §8---");
		p.sendMessage(Chat.PREFIX + "§e/kit list");
		p.sendMessage(Chat.PREFIX + "§e/kit show <Kitname>");
		p.sendMessage(Chat.PREFIX + "§e/kit add <Kitname>");
		p.sendMessage(Chat.PREFIX + "§e/kit remove <Kitname>");
		p.sendMessage(Chat.PREFIX + "§e/kit update <Kitname>");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!command.getName().equalsIgnoreCase("kit")) return true;
		if(!(sender instanceof Player)) {
			sender.sendMessage("Dieser Befehl kann nur von Spielern genutzt werden");
			return true;
		}
		Player p = (Player) sender;
		
		if(!p.hasPermission("oitct.setup")) {
			p.sendMessage(Chat.NO_PERM);
			return true;
		}
		
		if(args.length == 0) {
			printHelp(p);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("list")) {
			if(Kit.getKitList().isEmpty()) {
				p.sendMessage(Chat.PREFIX + "§bEs gibt noch keine Kits");
				return true;
			}
			p.sendMessage(Chat.PREFIX + "§7--- §bListe aller Kits §7---");
			for(Kit kit : Kit.getKitList()) {
				p.sendMessage(Chat.PREFIX + "§7- §d" + kit.getName());
			}
			return true;
		}
		
		if(args.length <= 1) {
			printHelp(p);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("show")) {
			Kit kit = Kit.getKit(args[1]);
			if(kit == null) {
				p.sendMessage(Chat.PREFIX + "§cDieses Kit gibt es nicht");
				return true;
			}
			kit.giveKit(p);
			p.sendMessage(Chat.PREFIX + "§bDu betrachtest das Kit §e" + kit.getName());
			return true;
		}
		
		if(args[0].equalsIgnoreCase("add")) {
			Kit kit = new Kit(args[1], p.getInventory(), p.getInventory().getArmorContents());
			if(kit.exists()) {
				p.sendMessage(Chat.PREFIX + "§cDieses Kit gibt es bereits");
				return true;
			}
			kit.saveKit();
			p.sendMessage(Chat.PREFIX + "§bDas Kit §e" + kit.getName() + " §bwurde erstellt");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("remove")) {
			Kit kit = Kit.getKit(args[1]);
			if(kit == null) {
				p.sendMessage(Chat.PREFIX + "§cDieses Kit gibt es nicht");
				return true;
			}
			kit.deleteKit();
			p.sendMessage(Chat.PREFIX + "§bDas Kit §e" + kit.getName() + " §bwurde gelöscht");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("update")) {
			Kit kit = Kit.getKit(args[1]);
			if(kit == null) {
				p.sendMessage(Chat.PREFIX + "§cDieses Kit gibt es nicht");
				return true;
			}
			kit.setInventory(p.getInventory());
			kit.saveKit();
			p.sendMessage(Chat.PREFIX + "§bDas Kit §e" + kit.getName() + " §bwurde aktualisiert");
			return true;
		}
		printHelp(p);
		return true;
	}

}
