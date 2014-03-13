package edu.nmt.minecraft.HomeWorldPlugin.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;

import edu.nmt.minecraft.HomeWorldPlugin.HomeWorldPlugin;

/**
 * class for separate thread. Saves accounts every once in a while
 * @author Matthew
 */
public class FileSavingThread extends Thread implements Listener{
	
	public boolean die = false;
	private boolean isRunning = true;
	public final long sleepTime = 1000 * 60 * 15; //15 minutes
	
	@Override
	public void run(){
		while(!die){
			
			long wait = 0;
			
			while (wait < sleepTime){
				
				//check for wilderness
				World wilderness = Bukkit.getWorld("Wilderness");
				if (wilderness != null){
					wilderness.setMonsterSpawnLimit(100);
					wilderness.setAnimalSpawnLimit(0);
					wilderness.setWaterAnimalSpawnLimit(0);
					wilderness.setTicksPerMonsterSpawns(20);
				}
				
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (die){
					isRunning = false;
					return;
				}
				wait += 3000;
			}
			
			//save everything
			HomeWorldPlugin.saveAll();
		}
		
	}
	
	public boolean isRunning(){
		return isRunning;
	}
}