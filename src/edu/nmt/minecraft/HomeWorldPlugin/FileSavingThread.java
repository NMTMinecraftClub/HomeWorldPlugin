package edu.nmt.minecraft.HomeWorldPlugin;

import org.bukkit.Bukkit;
import org.bukkit.World;


/**
 * class for separate thread. Saves accounts every once in a while
 * @author Matthew
 */
class FileSavingThread extends Thread {
	
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
	
}
