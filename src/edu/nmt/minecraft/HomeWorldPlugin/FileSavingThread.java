package edu.nmt.minecraft.HomeWorldPlugin;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import edu.nmt.minecraft.HomeWorldPlugin.economy.EconomyManager;
import edu.nmt.minecraft.HomeWorldPlugin.economy.InventoryAccount;

/**
 * class for separate thread. Saves accounts every once in a while
 * @author Matthew
 */
class FileSavingThread extends Thread implements Listener{
	
	HashSet<Location> tourches;
	public FileSavingThread(){
		tourches = new HashSet<Location>();
	}
	
	@Override
	public void run(){
		World wilderness = Bukkit.getWorld("Wilderness");
		if (wilderness != null){
			wilderness.setMonsterSpawnLimit(700);
			wilderness.setTicksPerMonsterSpawns(10);
			//wilderness.setTime(15000);
		}
		
		int j = 0;
		while(true){
			//save everything
			HomeWorldPlugin.saveAll();
			for (int i = 0; i < 10; i++){
				try {
					Thread.sleep(1000 * 60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if (wilderness != null){
					wilderness.setMonsterSpawnLimit(700);
					wilderness.setTicksPerMonsterSpawns(10);
					//wilderness.setTime(15000);
					//wilderness.
					
				}
				
			}
			World w = Bukkit.getWorld("Wilderness");
			j++;
			if (j > 6){
				if (w != null){
					for (Location loc: tourches){
					
						w.getBlockAt(loc).setType(Material.AIR);
					}
					tourches.clear();
					j = 0;
				}
				
			}
			
			
		}
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST)
    public void removeTourches(BlockPlaceEvent event) {
		
		if (event.getPlayer().getWorld().getName().equals("Wilderness")){
			if (event.getBlock().getType().equals(Material.TORCH)){
				tourches.add(event.getBlock().getLocation());
			}
		}
	}
	
}
