package de.theniclas.oitct.objects.fight;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.theniclas.oitct.utils.Title;

public class FightStartRunnable extends BukkitRunnable {

	private Fight fight;
	private int timer = 10;
	
	public FightStartRunnable(Fight fight) {
		this.fight = fight;
		run();
	}

	@Override
	public void run() {
		if(fight.getState() != State.STARTING) {
			for(Player p : fight.getWitnesses()) {
				Title.sendTitle(p, "", 0, 0, 0);
				Title.sendSubtitle(p, "", 0, 0, 0);
			}
			cancel();
		}
		if(timer == 10) {
			for(Player p : fight.getWitnesses()) {
				Title.sendSubtitle(p, "§6Kit§7: §f" + fight.getKit().getName(), 10, 20, 0);
				Title.sendTitle(p, "§e" + fight.getTeam1().getTeamName() + " §cvs§7. §e" + fight.getTeam2().getTeamName(), 10, 20, 0);
				p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f);
				fight.getKit().giveKit(p);
			}
			
		}
		if(timer <= 9 && timer >= 8) {
			for(Player p : fight.getWitnesses()) {
				Title.sendSubtitle(p, "§6Kit§7: §f" + fight.getKit().getName(), 0, 30, 0);
				Title.sendTitle(p, "§e" + fight.getTeam1().getTeamName() + " §cvs§7. §e" + fight.getTeam2().getTeamName(), 0, 30, 0);
			}
			
		}
		if(timer == 7) {
			for(Player p : fight.getWitnesses()) {
				Title.sendSubtitle(p, "§a" + timer, 0, 30, 0);
				Title.sendTitle(p, "§e" + fight.getTeam1().getTeamName() + " §cvs§7. §e" + fight.getTeam2().getTeamName(), 0, 30, 0);
			}
			
		}
		if(timer == 6) {
			for(Player p : fight.getWitnesses()) {
				Title.sendSubtitle(p, "§a" + timer, 0, 10, 10);
				Title.sendTitle(p, "§e" + fight.getTeam1().getTeamName() + " §cvs§7. §e" + fight.getTeam2().getTeamName(), 0, 10, 10);
			}
		}
		if(timer == 5 || timer == 4) {
			for(Player p : fight.getWitnesses()) {
				Title.sendTitle(p, "§a" + timer, 5, 10, 5);
				p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1.0f, 1.0f);
			}	
		}
		if(timer == 3) {
			for(Player p : fight.getWitnesses()) {
				Title.sendTitle(p, "§e3", 5, 10, 5);
				p.playSound(p.getLocation(), Sound.NOTE_BASS, 1.0f, 1.0f);
			}	
		}
		if(timer == 2) {
			for(Player p : fight.getWitnesses()) {
				Title.sendTitle(p, "§c2", 5, 10, 5);
				p.playSound(p.getLocation(), Sound.NOTE_BASS, 1.0f, 1.5f);
			}
		}
		if(timer == 1) {
			for(Player p : fight.getWitnesses()) {
				Title.sendTitle(p, "§41", 5, 10, 5);
				p.playSound(p.getLocation(), Sound.NOTE_BASS, 1.0f, 2.0f);
			}
		}
		if(timer == 0) {
			for(Player p : fight.getWitnesses()) {
				Title.sendTitle(p, "§5START", 5, 10, 5);
				p.playSound(p.getLocation(), Sound.ENDERDRAGON_HIT, 1.0f, 0.8f);
			}
			fight.setState(State.ONGOING);
			cancel();
		}
		timer--;
	}
	
	
}
