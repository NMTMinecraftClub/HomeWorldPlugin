package edu.nmt.minecraft.HomeWorldPlugin;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.Inventory;

import edu.nmt.minecraft.HomeWorldPlugin.economy.EconomyManager;
import edu.nmt.minecraft.HomeWorldPlugin.economy.InventoryAccount;

/**
 * class for separate thread. Saves accounts every once in a while
 * @author Matthew
 */
class FileSavingThread extends Thread implements Listener{
	
	@Override
	public void run(){
		World wilderness = Bukkit.getWorld("Wilderness");
		if (wilderness != null){
			wilderness.setMonsterSpawnLimit(700);
			wilderness.setTicksPerMonsterSpawns(10);
			//wilderness.setTime(15000);
		}

		while(true){
			//save everything
			HomeWorldPlugin.saveAll();
			for (int i = 0; i < 10; i++){
				wilderness = Bukkit.getWorld("Wilderness");
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

		}
	}
	
	public void wildernessDeath(PlayerDeathEvent event){
//		Player player = event.getEntity();
//		
//		//check if the player is in the wilderness
//		if (player.getWorld().getName().equals("Wilderness")){
//			
//			//get the enderchest inventory
//			Inventory inv = player.getEnderChest();
//			
//			int wealth = HomeWorldPlugin.economy.calculateWealth(inv);
//			
//			int percent = (int) ((Math.random() * 15) + 40));
//			
//			int reduce = (int) (wealth * percent / 100);
//			
//			HomeWorldPlugin.economy.reduceInventory(inv, reduce);
//			
//			player.sendMessage("You died in the wilderness.");
//			player.sendMessage("Your enderchest had a total value of $" + wealth);
//			player.sendMessage(percent + "% will be removed. You lost $" + reduce);
//			
//			
//			
//		}
//		
		
	}
	
	
}
