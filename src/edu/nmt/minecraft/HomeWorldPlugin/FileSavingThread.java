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

		while(true){
			//save everything
			HomeWorldPlugin.saveAll();
			for (int i = 0; i < 10; i++){
				try {
					Thread.sleep(1000 * 60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}

		}
	}
	
}
