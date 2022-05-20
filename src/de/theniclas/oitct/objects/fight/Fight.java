package de.theniclas.oitct.objects.fight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import de.theniclas.oitct.main.Main;
import de.theniclas.oitct.objects.Kit;
import de.theniclas.oitct.objects.Lobby;
import de.theniclas.oitct.objects.Map;
import de.theniclas.oitct.objects.Team;
import de.theniclas.oitct.utils.Chat;
import de.theniclas.oitct.utils.TempWorld;
import de.theniclas.oitct.utils.Title;
import de.theniclas.oitct.utils.UtilityMethods;

public class Fight {

	private State state;
	private Team team1;
	private Team team2;
	private Team winner;
	private Map map;
	private Kit kit;
	private int lives;

	private HashMap<Player, Integer> livesMap;
	private List<Player> witnesses;
	private List<Player> alive;
	private List<Block> placedBlocks;

	private Scoreboard scoreboard;

	private static HashMap<Player, Fight> spectators = new HashMap<>();
	private static HashMap<String, Fight> fights = new HashMap<>();

	public Fight(Team team1, Team team2, Map map, Kit kit, int lives) {
		this.team1 = team1;
		this.team2 = team2;
		this.map = map;
		this.kit = kit;
		this.lives = lives;

		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

		if(lives > 1) {
			this.livesMap = new HashMap<>();
		}
		this.witnesses = new ArrayList<>();
		this.alive = new ArrayList<>();
		this.placedBlocks = new ArrayList<>();
		this.state = State.NONE;

		updateHashMap();
	}

	public void start() {
		setState(State.STARTING);
		map.setWorld(TempWorld.getTempWorld(this));
		for(String uuid : team1.getOnlineMembers()) {
			Player p = Bukkit.getPlayer(UUID.fromString(uuid));
			p.getPlayer().spigot().respawn();
			UtilityMethods.fullHeal(p);
			alive.add(p.getPlayer());
			witnesses.add(p.getPlayer());
			if(livesMap != null) {
				livesMap.put(p.getPlayer(), lives);
				p.setLevel(lives);
				p.setExp(1);
			}
			p.getPlayer().teleport(map.getTeam1Spawns().get(team1.getOnlineMembers().indexOf(uuid)));
			p.getPlayer().setGameMode(GameMode.SURVIVAL);
		}
		for(String uuid : team2.getOnlineMembers()) {
			Player p = Bukkit.getPlayer(UUID.fromString(uuid));
			p.getPlayer().spigot().respawn();
			UtilityMethods.fullHeal(p);
			alive.add(p.getPlayer());
			witnesses.add(p.getPlayer());
			if(livesMap != null) {
				livesMap.put(p.getPlayer(), lives);
				p.setLevel(lives);
				p.setExp(1);
			}
			p.getPlayer().teleport(map.getTeam2Spawns().get(team2.getOnlineMembers().indexOf(uuid)));
			p.getPlayer().setGameMode(GameMode.SURVIVAL);
		}
		for(Player p : witnesses) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(!alive.contains(all))
					p.hidePlayer(all);
			}
		}
		createScoreboardTeam(team1, team2);

		new FightStartRunnable(this).runTaskTimer(Main.getPlugin(), 0, 20);
	}

	private void createScoreboardTeam(Team team1, Team team2) {
		org.bukkit.scoreboard.Team team = scoreboard.getTeam(team1.getTeamName() + team2.getTeamName());
		if(team == null) {
			team = scoreboard.registerNewTeam(team1.getTeamName() + team2.getTeamName());
		}
		team.setCanSeeFriendlyInvisibles(true);
		team.setAllowFriendlyFire(true);
		for(Player p : witnesses) {
			team.addEntry(p.getName());
			p.setScoreboard(scoreboard);
		}
	}

	public void end() {
		setState(State.ENDING);
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
		new BukkitRunnable() {

			@Override
			public void run() {
				for(Player p : witnesses) {
					p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
					Lobby.getLobby().sendToLobby(p);
					if(spectators.containsKey(p)) {
						spectators.remove(p);
					}
				}
				TempWorld.deleteTempWorld(Fight.this);
				fights.remove(team1.getTeamName());
				fights.remove(team2.getTeamName());
				witnesses.clear();
				alive.clear();
				placedBlocks.clear();
			}
		}.runTaskLater(Main.getPlugin(), 100);
	}

	public void kill(Player p) {
		if(livesMap != null && livesMap.containsKey(p)) {
			if(livesMap.get(p) >= 2) {
				if(team1.getMembers().contains(p.getUniqueId().toString()))
					p.teleport(map.getTeam1Spawns().get(Team.getTeam(p.getUniqueId()).getOnlineMembers().indexOf(p.getUniqueId().toString())));
				if(team2.getMembers().contains(p.getUniqueId().toString()))
					p.teleport(map.getTeam2Spawns().get(Team.getTeam(p.getUniqueId()).getOnlineMembers().indexOf(p.getUniqueId().toString())));
				livesMap.put(p, livesMap.get(p) - 1);
				UtilityMethods.fullHeal(p);
				p.setLevel(livesMap.get(p));
				p.setExp(1);
				kit.giveKit(p);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1 * 20, 1));
				return;
			}
			livesMap.remove(p);
		}
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
		livesMap.remove(p);
		witnesses.remove(p);
		if(livesMap != null && livesMap.containsKey(p))
			livesMap.remove(p);
		if(state == State.ENDING) {
			return;
		}

		for(Player witness : witnesses) {
			witness.sendMessage(Chat.PREFIX + "§e" + p.getName() + " §bhat den Kampf verlassen");
		}
		if(!alive.stream().anyMatch(element -> team1.getMembers().contains(element.getUniqueId().toString()))) {
			setWinner(team2);
			team2.addPoints(1);
			team1.addPoints(-1);
			end();
			return;
		}
		if(!alive.stream().anyMatch(element -> team2.getMembers().contains(element.getUniqueId().toString()))) {
			setWinner(team1);
			team1.addPoints(1);
			team2.addPoints(-1);
			end();
			return;
		}
	}

	public void removeSpectator(Player p) {
		witnesses.remove(p);
		spectators.remove(p);
		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}

	public void addSpectator(Player p) {
		witnesses.add(p);
		spectators.put(p, this);
		p.setScoreboard(scoreboard);
	}

	private void updateHashMap() {
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

	public HashMap<Player, Integer> getLives() {
		return livesMap;
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

	public Scoreboard getScoreboard() {
		return scoreboard;
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
