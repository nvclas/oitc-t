package de.theniclas.oitct.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.theniclas.oitct.main.Main;
import de.theniclas.oitct.objects.fight.Fight;
import de.theniclas.oitct.utils.Data;

public class Team {

	private String teamName;
	private int points;
	private List<String> members;
	public static final Data TEAMS = new Data(Main.getPlugin(), "teams.yml");

	public Team(String teamName, List<String> members) {
		this.teamName = teamName;
		this.members = members;
	}

	public Team(String teamName) {
		this.teamName = teamName;
		this.members = new ArrayList<>();
	}

	public void saveTeam() {
		TEAMS.getConfig().set("Team." + teamName + ".Members", members);
		TEAMS.saveFile();
	}

	public void deleteTeam() {
		TEAMS.getConfig().set("Team." + teamName, null);
		TEAMS.saveFile();
	}

	public boolean exists() {
		if(TEAMS.getConfig().get("Team." + teamName) != null) {
			return true;
		}
		return false;
	}

	public void sendAllMembers(String message) {
		for(String uuid : getOnlineMembers()) {
			Player target = Bukkit.getPlayer(UUID.fromString(uuid));
			target.sendMessage(message);
		}
	}
	
	public void fillMembers() {
		this.members = TEAMS.getConfig().getStringList("Team." + teamName + ".Members");
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	public void addMember(String uuid) {
		if(!isMember(uuid)) {
			members.add(uuid);
		}
	}

	public void removeMember(String uuid) {
		if(isMember(uuid)) {
			members.remove(uuid);
		}
	}

	public boolean isMember(String uuid) {
		if(members.contains(uuid)) {
			return true;
		}
		return false;
	}

	public List<String> getMembers() {
		return members;
	}
	
	public List<String> getOnlineMembers() {
		List<String> online = new ArrayList<>();
		for(String uuid : getMembers()) {
			if(Bukkit.getPlayer(UUID.fromString(uuid)) != null) {
				online.add(uuid);
			}
		}
		return online;
	}

	public String getTeamName() {
		return teamName;
	}
	
	public int getPoints() {
		return points;
	}
	
	public Fight getFight() {
		if(Fight.getFights().containsKey(getTeamName())) {
			return Fight.getFights().get(getTeamName());
		}
		return null;
	}
	
	public static Team getTeam(String name) {
		Team team = new Team(name);
		if(team.exists()) {
			team.fillMembers();
			team.setPoints(TEAMS.getConfig().getInt("Team." + team.getTeamName() + ".Points"));
			return team;
		}
		return null;
	}
	
	public static Team getTeam(UUID uuid) {
		if(TEAMS.getConfig().getConfigurationSection("Team") == null)
			return null;
		for(String teamName : TEAMS.getConfig().getConfigurationSection("Team").getKeys(false)) {
			if(TEAMS.getConfig().getList("Team." + teamName + ".Members").contains(uuid.toString()))
				return getTeam(teamName);
		}
		return null;
	}
	
	public static List<Team> getTeamList() {
		List<Team> teams = new ArrayList<>();
		if(TEAMS.getConfig().getConfigurationSection("Team") == null || TEAMS.getConfig().getConfigurationSection("Team").getKeys(false).isEmpty()) {
			return teams;
		}
		for(String teamName : TEAMS.getConfig().getConfigurationSection("Team").getKeys(false)) {
			teams.add(getTeam(teamName));
		}
		teams.sort((o1, o2) -> o1.getPoints() < o2.getPoints() ? 1 : o1.getPoints() == o2.getPoints() ? 0 : -1);
		return teams;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	public void setPoints(int points) {
		this.points = points;
		TEAMS.getConfig().set("Team." + teamName + ".Points", this.points);
		TEAMS.saveFile();
	}
	
	public void addPoints(int points) {
		this.points += points;
		TEAMS.getConfig().set("Team." + teamName + ".Points", this.points);
		TEAMS.saveFile();
	}

}
