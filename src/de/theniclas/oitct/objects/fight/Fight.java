package de.theniclas.oitct.objects.fight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.theniclas.oitct.main.Main;
import de.theniclas.oitct.objects.Kit;
import de.theniclas.oitct.objects.Lobby;
import de.theniclas.oitct.objects.Map;
import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.utils.Chat;
import de.theniclas.oitct.utils.Title;

public class Fight {

	private State state;
	private Team team1;
	private Team team2;
	private Team winner;
	private Map map;
	private Kit kit;

	private List<Player> witnesses;
	private List<Player> alive;
	private List<Block> placedBlocks;
	
	private static HashMap<Player, Fight> spectators = new HashMap<>();
	private static HashMap<String, Fight> fights = new HashMap<>();
	

	public Fight(Team team1, Team team2, Map map, Kit kit) {
		this.team1 = team1;
		this.team2 = team2;
		this.map = map;
		this.kit = kit;
		this.witnesses = new ArrayList<>();
		this.alive = new ArrayList<>();
		this.placedBlocks = new ArrayList<>();
		this.state = State.NONE;

		updateHashMap();
	}

	public void start() {
		setState(State.STARTING);
		for(String uuid : team1.getMembers()) {
			OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
			if(!op.isOnline())
				continue;
			op.getPlayer().spigot().respawn();
			alive.add(op.getPlayer());
			witnesses.add(op.getPlayer());
			op.getPlayer().teleport(map.getTeam1Spawns().get(team1.getMembers().indexOf(uuid)));
			op.getPlayer().setGameMode(GameMode.SURVIVAL);
		}
		for(String uuid : team2.getMembers()) {
			OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
			if(!op.isOnline())
				continue;
			op.getPlayer().spigot().respawn();
			alive.add(op.getPlayer());
			witnesses.add(op.getPlayer());
			op.getPlayer().teleport(map.getTeam2Spawns().get(team2.getMembers().indexOf(uuid)));
			op.getPlayer().setGameMode(GameMode.SURVIVAL);
		}
		for(Player p : witnesses) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(!alive.contains(all))
					p.hidePlayer(all);
			}
		}
		new BukkitRunnable() {

			int count = 10;

			@Override
			public void run() {
				if(getState() != State.STARTING) {
					for(Player p : witnesses) {
						Title.sendTitle(p, "", 0, 0, 0);
						Title.sendSubtitle(p, "", 0, 0, 0);
					}
					cancel();
				}
				if(count == 10) {
					for(Player p : witnesses) {
						Title.sendSubtitle(p, "§6Kit§7: §f" + getKit().getName(), 10, 20, 0);
						Title.sendTitle(p, "§e" + getTeam1().getTeamName() + " §cvs§7. §e" + getTeam2().getTeamName(), 10, 20, 0);
						p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f);
						kit.giveKit(p);
					}
					
				}
				if(count <= 9 && count >= 8) {
					for(Player p : witnesses) {
						Title.sendSubtitle(p, "§6Kit§7: §f" + getKit().getName(), 0, 30, 0);
						Title.sendTitle(p, "§e" + getTeam1().getTeamName() + " §cvs§7. §e" + getTeam2().getTeamName(), 0, 30, 0);
					}
					
				}
				if(count == 7) {
					for(Player p : witnesses) {
						Title.sendSubtitle(p, "§a" + count, 0, 30, 0);
						Title.sendTitle(p, "§e" + getTeam1().getTeamName() + " §cvs§7. §e" + getTeam2().getTeamName(), 0, 30, 0);
					}
					
				}
				if(count == 6) {
					for(Player p : witnesses) {
						Title.sendSubtitle(p, "§a" + count, 0, 10, 10);
						Title.sendTitle(p, "§e" + getTeam1().getTeamName() + " §cvs§7. §e" + getTeam2().getTeamName(), 0, 10, 10);
					}
				}
				if(count == 5 || count == 4) {
					for(Player p : witnesses) {
						Title.sendTitle(p, "§a" + count, 5, 10, 5);
						p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1.0f, 1.0f);
					}	
				}
				if(count == 3) {
					for(Player p : witnesses) {
						Title.sendTitle(p, "§e3", 5, 10, 5);
						p.playSound(p.getLocation(), Sound.NOTE_BASS, 1.0f, 1.0f);
					}	
				}
				if(count == 2) {
					for(Player p : witnesses) {
						Title.sendTitle(p, "§c2", 5, 10, 5);
						p.playSound(p.getLocation(), Sound.NOTE_BASS, 1.0f, 1.5f);
					}
				}
				if(count == 1) {
					for(Player p : witnesses) {
						Title.sendTitle(p, "§41", 5, 10, 5);
						p.playSound(p.getLocation(), Sound.NOTE_BASS, 1.0f, 2.0f);
					}
				}
				if(count == 0) {
					for(Player p : witnesses) {
						Title.sendTitle(p, "§5START", 5, 10, 5);
						p.playSound(p.getLocation(), Sound.ENDERDRAGON_HIT, 1.0f, 0.8f);
					}
					state = State.ONGOING;
					cancel();
				}
				count--;
			}
		}.runTaskTimer(Main.getPlugin(), 0, 20);
	}

	public void end() {
		setState(State.ENDING);
		winner.addPoints(1);
		for(Player p : witnesses) {
			if(getWinner().getMembers().contains(p.getUniqueId().toString())) {
				Title.sendTitle(p, "§2GEWONNEN", 20, 60, 20);
				Title.sendSubtitle(p, "§aHerzlichen Glückwunsch", 20, 60, 20);
				p.playSound(p.getLocation(), Sound.PORTAL_TRAVEL, 1.0f, 1.0f);
				continue;
			}
			if(!team1.getMembers().contains(p.getUniqueId().toString()) && !team2.getMembers().contains(p.getUniqueId().toString())) {
				Title.sendTitle(p, "§5ENDE", 20, 60, 20);
				Title.sendSubtitle(p, "§bGewonnen hat: §e" + getWinner().getTeamName(), 20, 60, 20);
				p.playSound(p.getLocation(), Sound.WOLF_HOWL, 1.0f, 1.0f);
				continue;
			}
			Title.sendTitle(p, "§4VERLOREN", 20, 60, 20);
			Title.sendSubtitle(p, "§cViel Glück beim nächsten Mal", 20, 60, 20);
			p.playSound(p.getLocation(), Sound.IRONGOLEM_DEATH, 1.0f, 1.0f);
			continue;
		}
		for(Block block : placedBlocks) {
			block.setType(Material.AIR);
		}
		new BukkitRunnable() {

			@Override
			public void run() {
				for(Player p : witnesses) {
					Lobby.sendToLobby(p);
					if(spectators.containsKey(p)) {
						spectators.remove(p);
					}
				}
				fights.remove(team1.getTeamName());
				fights.remove(team2.getTeamName());
				witnesses.clear();
				alive.clear();
				placedBlocks.clear();
			}
		}.runTaskLater(Main.getPlugin(), 100);
	}

	public void kill(Player p) {
		alive.remove(p);
		p.setGameMode(GameMode.SPECTATOR);
		if(!alive.stream().anyMatch(element -> team1.getMembers().contains(element.getUniqueId().toString()))) {
			setWinner(team2);
			end();
			return;
		}
		if(!alive.stream().anyMatch(element -> team2.getMembers().contains(element.getUniqueId().toString()))) {
			setWinner(team1);
			end();
			return;
		}
	}

	public void disqualify(Player p) {
		alive.remove(p);
		witnesses.remove(p);
		if(state == State.ENDING) {
			return;
		}
		
		for(Player witness : witnesses) {
			witness.sendMessage(Chat.PREFIX + "§e" + p.getName() + " §bhat den Kampf verlassen");
		}
		if(!alive.stream().anyMatch(element -> team1.getMembers().contains(element.getUniqueId().toString()))) {
			setWinner(team2);
			end();
			return;
		}
		if(!alive.stream().anyMatch(element -> team2.getMembers().contains(element.getUniqueId().toString()))) {
			setWinner(team1);
			end();
			return;
		}
	}

	public void removeSpectator(Player p) {
		witnesses.remove(p);
		spectators.remove(p);
	}

	public void addSpectator(Player p) {
		witnesses.add(p);
		spectators.put(p, this);
	}

	public void updateHashMap() {
		fights.put(team1.getTeamName(), this);
		fights.put(team2.getTeamName(), this);
	}

	public State getState() {
		return state;
	}

	public Team getTeam1() {
		return team1;
	}

	public Team getTeam2() {
		return team2;
	}

	public List<Player> getWitnesses() {
		return witnesses;
	}

	public List<Player> getAlive() {
		return alive;
	}
	
	public List<Block> getPlacedBlocks() {
		return placedBlocks;
	}
	
	public static HashMap<String, Fight> getFights() {
		return fights;
	}

	public static Fight getSpectatingFight(Player p) {
		if(spectators.containsKey(p)) {
			return spectators.get(p);
		}
		return null;
	}

	public Map getMap() {
		return map;
	}

	public Kit getKit() {
		return kit;
	}

	public Team getWinner() {
		return winner;
	}

	public void setWinner(Team winner) {
		this.winner = winner;
	}

	public void setState(State state) {
		this.state = state;
		updateHashMap();
	}

}
