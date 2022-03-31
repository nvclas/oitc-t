package de.theniclas.oitct.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.theniclas.oitct.objects.fight.Fight;
import de.theniclas.oitct.utils.Data;

public class Team {

	private String teamName;
	private List<String> members;
	public static final Data TREE = new Data("tree.yml");

	public Team(String teamName, List<String> members) {
		this.teamName = teamName;
		this.members = members;
	}

	public Team(String teamName) {
		this.teamName = teamName;
		this.members = new ArrayList<>();
	}

	public void saveTeam() {
		TREE.getConfig().set("Team." + teamName, members);
		TREE.saveFile();
	}

	public void deleteTeam() {
		TREE.getConfig().set("Team." + teamName, null);
		TREE.saveFile();
	}

	public boolean exists() {
		if(TREE.getConfig().get("Team." + teamName) != null) {
			return true;
		}
		return false;
	}

	public void sendAllMembers(String message) {
		for(String uuid : getMembers()) {
			if(!Bukkit.getOfflinePlayer(UUID.fromString(uuid)).isOnline()) {
				continue;
			}
			Player target = Bukkit.getPlayer(UUID.fromString(uuid));
			target.sendMessage(message);
		}
	}
	
	public void fillMembers() {
		this.members = TREE.getConfig().getStringList("Team." + getTeamName());
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

	public static boolean hasTeam(String uuid) {
		if(getTeamName(uuid) == null) {
			return false;
		}
		return true;
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
	
	public static String getTeamName(String uuid) {
		if(TREE.getConfig().getConfigurationSection("Team") == null)
			return null;
		for(String teamName : TREE.getConfig().getConfigurationSection("Team").getKeys(false)) {
			if(TREE.getConfig().getList("Team." + teamName).contains(uuid))
				return teamName;
		}
		return null;
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
			return team;
		}
		return null;
	}
	
	public static List<Team> getTeamList() {
		List<Team> teams = new ArrayList<>();
		if(TREE.getConfig().getConfigurationSection("Team") == null || TREE.getConfig().getConfigurationSection("Team").getKeys(false).isEmpty()) {
			return teams;
		}
		for(String teamName : TREE.getConfig().getConfigurationSection("Team").getKeys(false)) {
			teams.add(getTeam(teamName));
		}
		return teams;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

}
