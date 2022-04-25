package de.theniclas.oitct.objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import de.theniclas.oitct.objects.fight.Fight;

public class TempWorld {
	
	public static World getTempWorld(Map map, Fight fight) {
		String name = map.getName() + "_" + fight.getTeam1().getTeamName() + "_" + fight.getTeam2().getTeamName();
		return copyWorld(map.getWorld(), name);
	}
	
	public static void deleteTempWorld(Map map, Fight fight) {
		String name = map.getName() + "_" + fight.getTeam1().getTeamName() + "_" + fight.getTeam2().getTeamName();
		unloadWorld(Bukkit.getWorld(name));
		try {
			FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer().getPath() + "\\" + name));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void copyFileStructure(File source, File target){
	    try {
	        ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                    if (!target.mkdirs())
	                        throw new IOException("Couldn't create world directory!");
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    copyFileStructure(srcFile, destFile);
	                }
	            } else {
	                InputStream in = new FileInputStream(source);
	                OutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public static boolean unloadWorld(World world) {
        return world!=null && Bukkit.getServer().unloadWorld(world, false);
    }
	
	public static World copyWorld(World originalWorld, String newWorldName) {
        copyFileStructure(originalWorld.getWorldFolder(), new File(Bukkit.getWorldContainer(), newWorldName));
        return new WorldCreator(newWorldName).createWorld();
}
	
}
